package project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BoardSpaceTests {

	Player player;
  BoardSpace boardSpace;

  @BeforeEach
  void setup() {
    boardSpace = new BoardSpace("testName", "Location", 100, 10);
  }

  @Test
  void testGetName() {
    Assertions.assertEquals("testName", boardSpace.getName());
  }

	@Test
	void testGetType() {
		Assertions.assertEquals("Location", boardSpace.getType());
	}

  @Test
  void testGetCost() {
    Assertions.assertEquals(100, boardSpace.getCost());
  }

  @Test
  void testSetCost() {
    boardSpace.setCost(500);
    Assertions.assertEquals(500, boardSpace.getCost());
  }

  @Test
  void testGetRent() {
    Assertions.assertEquals(10, boardSpace.getRent());
  }

  @Test
  void testSetRent() {
    boardSpace.setRent(50);
    Assertions.assertEquals(50, boardSpace.getRent());
  }

  @Test
  void testGetOwner() {
    Assertions.assertNull(boardSpace.getOwner());
  }

  @Test
  void testSetOwner() {
    Player player = new Player("test", 1);
    boardSpace.setOwner(player);
    Assertions.assertEquals(player, boardSpace.getOwner());
  }

  @Test
  void testGetNumHouses() {
    Assertions.assertEquals(0, boardSpace.getNumHouses());
  }

  @Test
  void testSetNumHouses() {
    boardSpace.setNumHouses(2);
    Assertions.assertEquals(2, boardSpace.getNumHouses());
  }

  @Test
  void testGetNumHotels() {
    Assertions.assertEquals(0, boardSpace.getNumHotels());
  }

  @Test
  void testSetNumHotels() {
    boardSpace.setNumHotels(1);
    Assertions.assertEquals(1, boardSpace.getNumHotels());
  }

  @Test
  void testPayOwner() {
    Player player = new Player("testPlayer", 1);
    Player owner = new Player("testOwner", 2);
    Assertions.assertEquals(1500, player.getBalance());
    Assertions.assertEquals(1500, owner.getBalance());

    boardSpace.setOwner(owner);
    boardSpace.payOwner(player, 200);

    Assertions.assertEquals(1300, player.getBalance());
    Assertions.assertEquals(1700, owner.getBalance());
  }

  @Test
  void testPayBank() {
    Player player = new Player("testPlayer", 1);
    Assertions.assertEquals(1500, player.getBalance());

    boardSpace.payBank(player, 500);

    Assertions.assertEquals(1000, player.getBalance());
  }

  @Test
  void testSell() {
    Player player = new Player("testPlayer", 1);
    int playerInitialBalance = player.getBalance();
    int costOfProperty = 100;
    BoardSpace property = new BoardSpace("testProperty", "Property", costOfProperty, 50);
    property.setOwner(player);

    Assertions.assertEquals(player.getName(), property.getOwner().getName());

    property.sell();
    int playerCurrentBalance = player.getBalance();

    Assertions.assertNull(property.getOwner());
    Assertions.assertEquals(playerInitialBalance + costOfProperty, playerCurrentBalance);
  }

  @Test
  void testUpgradePropertyHousesNotEnoughMoney() {
    Player player = new Player("testPlayer", 1);
    BoardSpace property = new BoardSpace("test", "Property", 100, 50);
    property.setOwner(player);
    player.payBank(1450);

    Assertions.assertFalse(property.upgradeProperty());
  }

  @Test
  void testUpgradePropertyHousesEnoughMoney() {
    Player player = new Player("testPlayer", 1);
    BoardSpace property = new BoardSpace("test", "Property", 100, 50);
    property.setOwner(player);
    player.payBank(1400);

    Assertions.assertTrue(property.upgradeProperty());
    Assertions.assertEquals(1, property.getNumHouses());
  }

  @Test
  void testUpgradePropertyHotelNotEnoughMoney() {
    Player player = new Player("testPlayer", 1);
    BoardSpace property = new BoardSpace("test", "Property", 100, 50);
    property.setOwner(player);
    player.payBank(1350);
    property.setNumHouses(4);

    Assertions.assertFalse(property.upgradeProperty());
  }

  @Test
  void testUpgradePropertyHotelEnoughMoney() {
    Player player = new Player("testPlayer", 1);
    BoardSpace property = new BoardSpace("test", "Property", 100, 50);
    property.setOwner(player);
    player.payBank(1300);
    property.setNumHouses(4);

    Assertions.assertTrue(property.upgradeProperty());
    Assertions.assertEquals(1, property.getNumHotels());
  }

  @Test
  void testGetOriginalRent() {
    BoardSpace property = new BoardSpace("test", "Property", 100, 10, 50, null, 2, 0);
    Assertions.assertEquals(10, property.getOriginalRent());
  }

}
