package project;

public class Card {

  String effect;
  int amount;
  int location;
  boolean passGo;

  Card(String effect, int amount, int location, boolean passGo) {
    this.effect = effect;
    this.amount = amount;
    this.location = location;
    this.passGo = passGo;
  }

  public String getEffect() {
    return effect;
  }

  public int getAmount() {
    return amount;
  }

  public int getLocation() {
    return location;
  }

  public boolean passGo() {
    return passGo;
  }

  public String getOutputAndUpdatePlayer(Player player, BoardSpace[] board) {
    StringBuilder str = new StringBuilder();
    if (effect.equals("Collect")) {
      str.append("Collect: ");
      str.append(amount);
      player.setBalance(player.getBalance() + amount);
    } else if (effect.equals("Pay")) {
      str.append("Pay: ");
      str.append(amount);
      player.payBank(amount);
    } else if (effect.equals("Jail")) {
      str.append("Move to Jail!");
      player.setLocation(location);
      player.goJail();
    } else if (effect.equals("JailFree")){
      str.append("You received a get out of jail free card!");
      player.setJailFreeCard(true);
    } else {
      str.append("Move to: " + board[location].getName());
      player.setLocation(location);
      if (passGo) {
        player.setBalance(player.getBalance() + 200);
      }
    }

    return str.toString();
  }

}
