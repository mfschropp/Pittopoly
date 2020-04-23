package project;

import java.util.ArrayList;
import java.util.List;

public class Player {

  String name;
  int location;
  int balance;
  int playerNum;
  boolean inJail;
  boolean jailFreeCard;
  int jailAttempt;
  List<BoardSpace> ownedProperties;

  Player(String name, int playerNum) {
    this.name = name;
    location = 0;
    balance = 1500;
    this.playerNum = playerNum;
    inJail = false;
    jailFreeCard = false;
    jailAttempt = 0;
    ownedProperties = new ArrayList<BoardSpace>();
  }

  Player(String name, int location, int balance, int playerNum,
      boolean inJail, boolean jailFreeCard, int jailAttempt) {
    this.name = name;
    this.location = location;
    this.balance = balance;
    this.playerNum = playerNum;
    this.inJail = inJail;
    this.jailFreeCard = jailFreeCard;
    this.jailAttempt = jailAttempt;
    ownedProperties = new ArrayList<BoardSpace>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLocation() {
    return location;
  }

  public void setLocation(int location) {
    this.location = location;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public int getPlayerNum() {
    return playerNum;
  }

  public void setPlayerNum(int playerNum) {
    this.playerNum = playerNum;
  }

  public boolean getJail() {
    return inJail;
  }

  public void goJail() {
    this.inJail = true;
  }

  public void exitJail() {
    this.inJail = false;
  }

  public boolean jailFreeCard() {
    return jailFreeCard;
  }

  public void setJailFreeCard(boolean jailFreeCard) {
    this.jailFreeCard = jailFreeCard;
  }

  public int getJailAttempt() {
    return jailAttempt;
  }

  public void setJailAttempt(int jailAttempt) {
    this.jailAttempt = jailAttempt;
  }

  public void addOwnedProperty(BoardSpace property) {
    ownedProperties.add(property);
  }

  public void removeOwnedProperty(BoardSpace property) {
    ownedProperties.remove(property);
  }

  public List<BoardSpace> getOwnedProperties() {
    return ownedProperties;
  }

  public void payBank(int amount) {
    balance = amount > balance ? 0 : balance - amount;
  }

  public void payPlayer(Player player, int amount) {
    if (amount > balance) {
      player.setBalance(player.getBalance() + balance);
      this.balance = 0;
    } else {
      player.setBalance(player.getBalance() + amount);
      balance = balance - amount;
    }
  }

  public void purchaseProperty(BoardSpace property) {
    if (balance < property.getCost()) {
      System.out.println("You do not have enough for this property!");
    } else {
      payBank(property.getCost());
      addOwnedProperty(property);
      property.setOwner(this);
      System.out.println("Purchased " + property.getName() + "!");
    }
  }

}
