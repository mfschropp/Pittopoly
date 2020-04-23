package project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.Scanner;

import static org.mockito.Mockito.*;
import org.mockito.Mock;

@SpringBootTest
@ActiveProfiles("test")
class GameTests {

  BufferedReader bufferedReader;
  Game game;
  Scanner scan;

  @BeforeEach
  void setup() throws IOException {
    scan = new Scanner(System.in);
    game = new Game(scan);
    bufferedReader = mock(BufferedReader.class);
    when(bufferedReader.readLine())
        .thenReturn("TestName, Location, 50, 6")
        .thenReturn(null);
  }

  @Test
  void testInitializeBoard() {
    BoardSpace expectedBoardSpace = new BoardSpace("TestName", "Location", 50, 6);

    BoardSpace[] actualBoard = game.initializeBoard(bufferedReader);

    Assertions.assertEquals(expectedBoardSpace.getName(), actualBoard[0].getName());
  }

  @Test
  void testInitializeBoardIOException() throws IOException {
    when(bufferedReader.readLine())
        .thenThrow(new IOException());

    Assertions.assertNull(game.initializeBoard(bufferedReader));
  }

  @Test
  void testInitializeChestDeckPassGoTrue() throws IOException {
    when(bufferedReader.readLine())
        .thenReturn("Collect, 50, 0, 1")
        .thenReturn(null);

    Card expectedCard = new Card("Collect", 50, 0, true);

    Card[] actualChestDeck = game.initializeChestDeck(bufferedReader);

    Assertions.assertEquals(expectedCard.getEffect(), actualChestDeck[0].getEffect());
  }

  @Test
  void testInitializeChestDeckPassGoFalse() throws IOException {
    when(bufferedReader.readLine())
        .thenReturn("Collect, 50, 0, 0")
        .thenReturn(null);

    Card expectedCard = new Card("Collect", 50, 0, false);

    Card[] actualChestDeck = game.initializeChestDeck(bufferedReader);

    Assertions.assertEquals(expectedCard.getEffect(), actualChestDeck[0].getEffect());
  }

  @Test
  void testInitializeChestDeckIOException() throws IOException {
    when(bufferedReader.readLine())
        .thenThrow(new IOException());

    Assertions.assertNull(game.initializeChestDeck(bufferedReader));
  }

  @Test
  void testInitialieChanceDeckPassGoTrue() throws IOException {
    when(bufferedReader.readLine())
        .thenReturn("Move, 0, 20, 1")
        .thenReturn(null);

    Card expectedCard = new Card("Move", 0, 20, true);

    Card[] actualChanceDeck = game.initializeChanceDeck(bufferedReader);

    Assertions.assertEquals(expectedCard.getEffect(), actualChanceDeck[0].getEffect());
  }

  @Test
  void testInitialieChanceDeckPassGoFalse() throws IOException {
    when(bufferedReader.readLine())
        .thenReturn("Move, 0, 20, 0")
        .thenReturn(null);

    Card expectedCard = new Card("Move", 0, 20, false);

    Card[] actualChanceDeck = game.initializeChanceDeck(bufferedReader);

    Assertions.assertEquals(expectedCard.getEffect(), actualChanceDeck[0].getEffect());
  }

  @Test
  void testInitializeChanceDeckIOException() throws IOException {
    when(bufferedReader.readLine())
        .thenThrow(new IOException());

    Assertions.assertNull(game.initializeChanceDeck(bufferedReader));
  }




}
