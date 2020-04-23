package project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CardTests {

  Card card;

  @BeforeEach
  void setup() {
    card = new Card("testEffect", 100, 1, false);
  }

  @Test
  void testGetEffect() {
    Assertions.assertEquals("testEffect", card.getEffect());
  }

  @Test
  void testGetAmount() {
    Assertions.assertEquals(100, card.getAmount());
  }

  @Test
  void testGetLocation() {
    Assertions.assertEquals(1, card.getLocation());
  }

  @Test
  void testPassGo() {
    Assertions.assertFalse(card.passGo());
  }

  @Test
  void testGetOutputAndUpdatePlayerCollect() {
    Player player = new Player("testPlayer", 1);
    BoardSpace[] board = new BoardSpace[2];
    card = new Card("Collect", 100, -1, false);
    String actual = card.getOutputAndUpdatePlayer(player, board);
    String expected = "Collect: 100";
    Assertions.assertEquals(actual, expected);
  }

  @Test
  void testGetOutputAndUpdatePlayerPay() {
    Player player = new Player("testPlayer", 1);
    BoardSpace[] board = new BoardSpace[2];
    card = new Card("Pay", 100, -1, false);
    String actual = card.getOutputAndUpdatePlayer(player, board);
    String expected = "Pay: 100";
    Assertions.assertEquals(actual, expected);
  }

  @Test
  void testGetOutputAndUpdatePlayerJail() {
    Player player = new Player("testPlayer", 1);
    BoardSpace[] board = new BoardSpace[2];
    card = new Card("Jail", 0, -1, false);
    String actual = card.getOutputAndUpdatePlayer(player, board);
    String expected = "Move to Jail!";
    Assertions.assertEquals(actual, expected);
  }

  @Test
  void testGetOutputAndUpdatePlayerJailFree() {
    Player player = new Player("testPlayer", 1);
    BoardSpace[] board = new BoardSpace[2];
    card = new Card("JailFree", 0, -1, false);
    String actual = card.getOutputAndUpdatePlayer(player, board);
    String expected = "You received a get out of jail free card!";
    Assertions.assertEquals(actual, expected);
    Assertions.assertTrue(player.jailFreeCard());
  }

  @Test
  void testGetOutputAndUpdatePlayerMovePassGo() {
    Player player = new Player("testPlayer", 1);
    BoardSpace[] board = new BoardSpace[2];
    BoardSpace boardSpace1 = new BoardSpace("one", "Property", 10, 10);
    BoardSpace boardSpace2 = new BoardSpace("two", "Property", 10, 10);
    board[0] = boardSpace1;
    board[1] = boardSpace2;
    card = new Card("Move", 0, 1, true);
    int balanceBeforeGo = player.getBalance();
    String actual = card.getOutputAndUpdatePlayer(player, board);
    int balanceAfterGo = player.getBalance();
    String expected = "Move to: two";
    Assertions.assertEquals(actual, expected);
    Assertions.assertEquals(balanceAfterGo - 200, balanceBeforeGo);
  }

  @Test
  void testGetOutputAndUpdatePlayerMoveDoNotPassGo() {
    Player player = new Player("testPlayer", 1);
    BoardSpace[] board = new BoardSpace[2];
    BoardSpace boardSpace1 = new BoardSpace("one", "Property", 10, 10);
    BoardSpace boardSpace2 = new BoardSpace("two", "Property", 10, 10);
    board[0] = boardSpace1;
    board[1] = boardSpace2;
    card = new Card("Move", 0, 1, false);
    int balanceBeforeGo = player.getBalance();
    String actual = card.getOutputAndUpdatePlayer(player, board);
    int balanceAfterGo = player.getBalance();
    String expected = "Move to: two";
    Assertions.assertEquals(actual, expected);
    Assertions.assertEquals(balanceAfterGo, balanceBeforeGo);
  }

}
