package project;

import java.util.Random;

public class Dice {

  int maxValue;
  int minValue;

  Dice() {
    maxValue = 6;
    minValue = 1;
  }

  public int roll() {
    Random rand = new Random();
    return rand.nextInt((maxValue - minValue) + 1) + minValue;
  }

}
