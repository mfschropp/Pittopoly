package project;

public class BoardSpace {

  String name;
  String type;
  int cost;
  int originalRent;
  int currentRent;
  Player owner;
  int numHouses;
  int numHotels;

  BoardSpace(String name, String type, int cost, int originalRent) {
    this.name = name;
    this.type = type;
    this.cost = cost;
    this.originalRent = originalRent;
    currentRent = this.originalRent;
    owner = null;
    numHouses = 0;
    numHotels = 0;
  }

  BoardSpace(String name, String type, int cost, int originalRent,
      int currentRent, Player owner, int numHouses, int numHotels) {
    this.name = name;
    this.type = type;
    this.cost = cost;
    this.originalRent = originalRent;
    this.currentRent = currentRent;
    this.owner = owner;
    this.numHouses = numHouses;
    this.numHotels = numHotels;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public int getCost() {
    return cost;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public int getOriginalRent() {
    return originalRent;
  }

  public int getRent() {
    return currentRent;
  }

  public void setRent(int rent) {
    this.currentRent = rent;
  }

  public Player getOwner() {
    return owner;
  }

  public void setOwner(Player owner) {
    this.owner = owner;
  }

  public int getNumHouses() {
    return numHouses;
  }

  public void setNumHouses(int numHouses) {
    this.numHouses = numHouses;
  }

  public int getNumHotels() {
    return numHotels;
  }

  public void setNumHotels(int numHotels) {
    this.numHotels = numHotels;
  }

  public void payOwner(Player player, int amount) {
    player.payPlayer(owner, amount);
  }

  public void payBank(Player player, int amount) {
    player.payBank(amount);
  }

  public void sell() {
    owner.setBalance(owner.getBalance() + cost);
    owner.removeOwnedProperty(this);
    owner = null;
    this.currentRent = originalRent;
  }

  public boolean upgradeProperty() {
    if (numHouses <= 3) {
      if (owner.getBalance() < 100) {
        System.out.println("You do not have enough money for a house.");
        return false;
      } else {
        owner.payBank(100);
        numHouses++;
        currentRent = currentRent * 3;
        return true;
      }
    } else {
      if (owner.getBalance() < 200) {
        System.out.println("You do not have enough money for a hotel.");
        return false;
      } else {
        owner.payBank(200);
        numHotels++;
        currentRent = originalRent * 100;
        return true;
      }
    }
  }

}
