package project;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.UUID;

public class Game {

  Player[] players = new Player[4];
  BoardSpace[] board;
  Card[] chestDeck;
  Card[] chanceDeck;
  BufferedReader bufferedReader;
  InputStream inputStream;

  String BOARDSPACESTEXT = "/boardspaces.txt";
  String CHESTCARDSTEXT = "/chestcards.txt";
  String CHANCECARDSTEXT = "/chancecards.txt";

  Scanner scan;

  Game(Scanner scan) {
    this.scan = scan;
  }

  public void menu() {
    System.out.println("Welcome to Pittopoly, what would you like to do?");
		System.out.println("[1]: Start New Game");
		System.out.println("[2]: Load Game");
		System.out.println("[0]: Exit Game");

		int s = scan.nextInt();

		switch(s) {
			case 1:
				initialize(null, BOARDSPACESTEXT, CHESTCARDSTEXT, CHANCECARDSTEXT);
				start();
				break;
      case 2:
        load();
        start();
        break;
			default:
				exit(0);
    }
  }

  public void exit(int n) {
		if (n == 0) {
			System.out.println("Exiting...");
			System.exit(0);
		}
	}

  public void initialize(String playerText, String boardText,
      String chestText, String chanceText) {

    if (playerText == null) {
      players = initializePlayers();
    } else {
      try {
        inputStream = new FileInputStream(playerText);
      } catch (Exception e) {
        players = initializePlayers();
      }
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      players = initializePlayers(bufferedReader);
    }

    try {
      inputStream = new FileInputStream(boardText);
    } catch (Exception e) {
      inputStream = Game.class.getResourceAsStream(boardText);
    }
    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    board = initializeBoard(bufferedReader);

    inputStream = Game.class.getResourceAsStream(chestText);
    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    chestDeck = initializeChestDeck(bufferedReader);

    inputStream = Game.class.getResourceAsStream(chanceText);
    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    chanceDeck = initializeChanceDeck(bufferedReader);

  }

  public void load() {
    System.out.println("\nEnter your save ID.");
    String s = scan.next();
    Storage storage = StorageOptions.getDefaultInstance().getService();
    Loader loader = new Loader(storage, "pittopoly-saves");
    loader.loadPlayers(s);
    loader.loadBoard(s);
    initialize("playersaves.txt", "boardsaves.txt", CHESTCARDSTEXT, CHANCECARDSTEXT);
  }

  public void start() {
    System.out.println("Starting the game!");
    startTurn(players);
  }

  public void startTurn(Player[] players) {
    int i = 0;
    Player currentPlayer = players[i];
    boolean running = true;

    while (running) {
      System.out.println("\n" + currentPlayer.getName() + ", it is your turn.");
      System.out.println("You have: " + currentPlayer.getBalance() + " dollars.");
      if (currentPlayer.getJail()) {
        running = jailTurn(currentPlayer);
      } else {
        running = normalTurn(currentPlayer);
      }

      i = (i + 1) % 4;
      currentPlayer = players[i];
    }

    initiateSave();
    exit(0);
  }

  public boolean jailTurn(Player player) {
    System.out.println("\nOh no, you are in jail!");
    System.out.println("[1]: Attempt to leave jail.");
    System.out.println("[0]: Save and Exit.");
    int choice = scan.nextInt();

    if (choice == 1) {
      if (player.jailFreeCard()) {
        return normalTurn(player);
      } else if (player.getJailAttempt() >= 3) {
        return normalTurn(player);
      } else {
        Dice dice1 = new Dice();
        Dice dice2 = new Dice();
        int val1 = dice1.roll();
        int val2 = dice2.roll();
        System.out.println("You rolled: " + val1 + " and " + val2);
        if (val1 == val2) {
          System.out.println("You made it out of jail, continue your normal turn.");
          player.exitJail();
          return normalTurn(player);
        } else {
          player.setJailAttempt(player.getJailAttempt() + 1);
          return true;
        }
      }
    } else {
      return false;
    }

  }

  public boolean normalTurn(Player player) {
    System.out.println("\nWhat would you like to do?");
    System.out.println("[1]: Roll to move.");
    System.out.println("[2]: Purchase a house or hotel.");
    System.out.println("[3]: Sell an owned property.");
    System.out.println("[0]: Save and Exit.");
    int choice = scan.nextInt();

    switch (choice) {
      case 1:
        move(player, 0);
        return true;
      case 2:
        if (purchaseUpgrade(player)) {
          System.out.println("Successfully upgraded property.");
        }
        return normalTurn(player);
      case 3:
        if (sellProperty(player)) {
          System.out.println("Successfully sold property.");
        }
        return normalTurn(player);
      case 0:
        return false;
      default:
        return normalTurn(player);
    }
  }

  public void move(Player player, int doubleCount) {
    Dice dice = new Dice();
    Dice dice2 = new Dice();
    int val1 = dice.roll();
    int val2 = dice2.roll();

    if (doubleCount >= 3) {
      System.out.println("\n3 doubles in a row, move to Jail!");
      movePlayerToJail(player);
    } else {
      System.out.println("\nYou rolled a: " + val1 +" and " + val2);

      int newPlayerLocation = setPlayerLocation(player, val1 + val2);
      BoardSpace currentSpace = board[newPlayerLocation];

      doAction(player, currentSpace);

      if (val1 == val2) {
        move(player, doubleCount + 1);
      }
    }
  }

  public void doAction(Player player, BoardSpace currentSpace) {
    if (currentSpace.getType().equals("Property") || currentSpace.getType().equals("Railroad")) {
      if (currentSpace.getOwner() == null) {
        offerPropertyForPurchase(player, currentSpace);
      } else {
        payRentToOwner(player, currentSpace);
      }
    } else if (currentSpace.getType().equals("Tax")) {
      payTax(player);
    } else if (currentSpace.getType().equals("ChestCard")) {
      drawChestCard(player);
    } else if (currentSpace.getType().equals("ChanceCard")) {
      drawChanceCard(player);
    } else if (currentSpace.getType().equals("Jail")) {
      System.out.println("You landed on Jail! But you are just visiting.");
    } else if (currentSpace.getType().equals("GoToJail")) {
      System.out.println("Go to Jail");
      movePlayerToJail(player);
    } else if (currentSpace.getType().equals("Go")) {
      System.out.println("You landed on Go! Next roll will bring payday!");
    } else if (currentSpace.getType().equals("FreeParking")) {
      System.out.println("Free parking!");
    }
  }

  public void offerPropertyForPurchase(Player player, BoardSpace space) {
    System.out.println("\nYou landed on " + space.getName() + "!");
    System.out.println("This is available for: " + space.getCost() + "."
        + " Rent is initially: " + space.getRent());
    System.out.println("[1]: Purchase");
    System.out.println("[2]: Pass");
    int choice = scan.nextInt();
    if (choice == 1) {
      player.purchaseProperty(space);
    } else {
      System.out.println("Passing on the property.");
    }
  }

  public void payRentToOwner(Player player, BoardSpace space) {
    System.out.println("You landed on: " + space.getName()
        + ", owned by: " + space.getOwner().getName());
    System.out.println("Paying the owner: " + space.getRent());
    space.payOwner(player, space.getRent());
  }

  public void payTax(Player player) {
    System.out.println("\nYou landed on Tax! Time to pay taxes ($150).");
    player.payBank(150);
  }

  public void drawChestCard(Player player) {
    System.out.println("\nCommunity Chest space!");
    int random = new Random().nextInt(chestDeck.length);
    Card card = chestDeck[random];
    String output = card.getOutputAndUpdatePlayer(player, board);
    System.out.println(output);
  }

  public void drawChanceCard(Player player) {
    System.out.println("\nChance space!");
    int random = new Random().nextInt(chanceDeck.length);
    Card card = chanceDeck[random];
    String output = card.getOutputAndUpdatePlayer(player, board);
    System.out.println(output);
  }

  public void movePlayerToJail(Player player) {
    player.setLocation(10);
    player.goJail();
  }

  public int setPlayerLocation(Player player, int roll) {
    int newLocation = (player.getLocation() + roll) % 40;
    player.setLocation(newLocation);
    return newLocation;
  }

  public boolean purchaseUpgrade(Player player) {
    if (player.getOwnedProperties().isEmpty()) {
      System.out.println("You do not have any owned properties, please select another option.");
      return false;
    } else {
      System.out.println("Please type the number of the property you wish to upgrade.");
      int i = 1;
      for (BoardSpace prop : player.getOwnedProperties()) {
        System.out.println(i + ": " + prop.getName());
        i++;
      }
      int choice = scan.nextInt();
      BoardSpace propertyToUpgrade = player.getOwnedProperties().get(choice - 1);
      return propertyToUpgrade.upgradeProperty();
    }
  }

  public boolean sellProperty(Player player) {
    if (player.getOwnedProperties().isEmpty()) {
      System.out.println("You do not have any owned properties, please select another option.");
      return false;
    } else {
      System.out.println("Please type the number of the property you wish to sell.");
      int i = 1;
      for (BoardSpace prop : player.getOwnedProperties()) {
        System.out.println(i + ": " + prop.getName());
        i++;
      }
      int choice = scan.nextInt();
      BoardSpace propertyToSell = player.getOwnedProperties().get(choice - 1);
      return verifySellProperty(propertyToSell);
    }
  }

  public boolean verifySellProperty(BoardSpace property) {
    System.out.println("You chose: " + property.getName() + " to sell.");
    System.out.println("This would sell for: " + property.getCost() + " dollars.");
    System.out.println("[1]: Sell");
    System.out.println("[2]: Cancel");
    int choice = scan.nextInt();

    switch (choice) {
      case 1:
        property.sell();
        return true;
      case 2:
        return false;
      default:
        return verifySellProperty(property);
    }
  }

  public Player[] initializePlayers() {

    Player player;
    Player[] newPlayers = new Player[4];
    for (int i = 1; i <= players.length; i++) {
      System.out.println("Enter your player name: ");
      String s = scan.next();
      player = new Player(s, i);
      newPlayers[i-1] = player;
    }

    return newPlayers;

  }

  public Player[] initializePlayers(BufferedReader bufferedReader) {
    Player player;
    Player[] newPlayers = new Player[4];
    int loc = 0;

    try {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        String[] items = line.split(", ");
        boolean inJail = Integer.parseInt(items[4]) == 0 ? false : true;
        boolean jailFreeCard = Integer.parseInt(items[5]) == 0 ? false : true;
        player = new Player(items[0],
                            Integer.parseInt(items[1]),
                            Integer.parseInt(items[2]),
                            Integer.parseInt(items[3]),
                            inJail,
                            jailFreeCard,
                            Integer.parseInt(items[6]));
        newPlayers[loc] = player;
        loc++;
      }
    } catch (Exception e) {
      System.out.println("Failed to intiate players. Starting default players.");
      return initializePlayers();
    }

    return newPlayers;
  }

  public BoardSpace[] initializeBoard(BufferedReader bufferedReader) {

    BoardSpace boardSpace;
    BoardSpace[] newBoard = new BoardSpace[40];
    int loc = 0;

    try {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        String[] items = line.split(", ");
        if (items.length == 4) {
          boardSpace = new BoardSpace(items[0],
                                      items[1],
                                      Integer.parseInt(items[2]),
                                      Integer.parseInt(items[3]));
        } else {
          boardSpace = new BoardSpace(items[0],
                                      items[1],
                                      Integer.parseInt(items[2]),
                                      Integer.parseInt(items[3]),
                                      Integer.parseInt(items[4]),
                                      getPlayer(items[5]),
                                      Integer.parseInt(items[6]),
                                      Integer.parseInt(items[7]));
          if (boardSpace.getOwner() != null) {
            boardSpace.getOwner().addOwnedProperty(boardSpace);
          }
        }

        newBoard[loc] = boardSpace;
        loc++;
      }

    } catch (IOException e) {
      System.out.println("Error, unable to create board.");
      newBoard = null;
    }

    return newBoard;

  }

  public Player getPlayer(String name) {
    for (Player player : players) {
      if (player.getName().equals(name)) {
        return player;
      }
    }
    return null;
  }

  public Card[] initializeChestDeck(BufferedReader bufferedReader) {

    Card card;
    Card[] newChestDeck = new Card[16];
    int loc = 0;
    boolean passGo = false;

    try {
      String line;

      while((line = bufferedReader.readLine()) != null) {
        String[] items = line.split(", ");
        passGo = Integer.parseInt(items[3]) == 0 ? false : true;
        card = new Card(items[0], Integer.parseInt(items[1]),
            Integer.parseInt(items[2]), passGo);
        newChestDeck[loc] = card;
        loc++;
      }

    } catch (Exception e) {
      System.out.println("Error, unable to create community chest deck.");
      newChestDeck = null;
    }

    return newChestDeck;

  }

  public Card[] initializeChanceDeck(BufferedReader bufferedReader) {

    Card card;
    Card[] newChanceDeck = new Card[16];
    int loc = 0;
    boolean passGo = false;

    try {
      String line;

      while((line = bufferedReader.readLine()) != null) {
        String[] items = line.split(", ");
        passGo = Integer.parseInt(items[3]) == 0 ? false : true;
        card = new Card(items[0], Integer.parseInt(items[1]),
            Integer.parseInt(items[2]), passGo);
        newChanceDeck[loc] = card;
        loc++;
      }

    } catch (Exception e) {
      System.out.println("Error, unable to create chance deck.");
      newChanceDeck = null;
    }

    return newChanceDeck;

  }

  public void initiateSave() {
    Storage storage = StorageOptions.getDefaultInstance().getService();
    Saver saver = new Saver(players, board, storage, "pittopoly-saves");
    String id = UUID.randomUUID().toString();
    System.out.println("Your Save ID is: " + id);
    saver.writeToSave(id);
  }

}
