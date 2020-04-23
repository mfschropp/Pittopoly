package project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DiceTests {

  Dice dice;

  @BeforeEach
  void setup() {
    dice = new Dice();
  }

  @Test
  void testRoll() {
    int value = dice.roll();
    Assertions.assertTrue(1 <= value && value <= 6);
  }

}
