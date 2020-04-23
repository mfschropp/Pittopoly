package project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PlayerTests {

	Player player;

  @BeforeEach
  void setup() {
    player = new Player("testPlayer", 1);
  }

	@Test
	void testGetName() {
		Assertions.assertEquals("testPlayer", player.getName());
	}

  @Test
  void testSetName() {
    player.setName("testNameUpdate");
    Assertions.assertEquals("testNameUpdate", player.getName());
  }

  @Test
  void testGetLocation() {
    Assertions.assertEquals(0, player.getLocation());
  }

  @Test
  void testSetLocation() {
    player.setLocation(1);
    Assertions.assertEquals(1, player.getLocation());
  }

  @Test
  void testGetBalance() {
    Assertions.assertEquals(1500, player.getBalance());
  }

  @Test
  void testSetBalance() {
    player.setBalance(1000);
    Assertions.assertEquals(1000, player.getBalance());
  }

  @Test
  void testGetPlayerNum() {
    Assertions.assertEquals(1, player.getPlayerNum());
  }

  @Test
  void testSetPlayerNum() {
    player.setPlayerNum(2);
    Assertions.assertEquals(2, player.getPlayerNum());
  }

  @Test
  void testGetJail() {
    Assertions.assertFalse(player.getJail());
  }

  @Test
  void testGoJail() {
    player.goJail();
    Assertions.assertTrue(player.getJail());
  }

  @Test
  void testExitJail() {
    player.goJail();
    Assertions.assertTrue(player.getJail());
    player.exitJail();
    Assertions.assertFalse(player.getJail());
  }

  @Test
  void testPayBankEnoughBalance() {
    player.payBank(1000);
    Assertions.assertEquals(500, player.getBalance());
  }

  @Test
  void testPayBankNotEnoughBalance() {
    player.payBank(2000);
    Assertions.assertEquals(0, player.getBalance());
  }

  @Test
  void testPayPlayerEnoughBalance() {
    Player player2 = new Player("Player2", 2);
    player.payPlayer(player2, 500);
    Assertions.assertEquals(1000, player.getBalance());
    Assertions.assertEquals(2000, player2.getBalance());
  }

  @Test
  void testPayPlayerNotEnoughBalance() {
    Player player2 = new Player("Player2", 2);
    player.payPlayer(player2, 2000);
    Assertions.assertEquals(0, player.getBalance());
    Assertions.assertEquals(3000, player2.getBalance());
  }

	@Test
	void testGetOwnedProperties() {
		player.addOwnedProperty(new BoardSpace("test", "Property", 100, 10));
		Assertions.assertEquals(1, player.getOwnedProperties().size());
	}

	@Test
	void testGetJailAttempt() {
		player.setJailAttempt(2);
		Assertions.assertEquals(2, player.getJailAttempt());
	}

	@Test
	void testJailFreeCard() {
		Assertions.assertFalse(player.jailFreeCard());
	}

	@Test
	void testSetJailFreeCard() {
		player.setJailFreeCard(true);
		Assertions.assertTrue(player.jailFreeCard());
	}

	@Test
	void testPurchasePropertyNotEnough() {
		Player player = new Player("test", 0, 0, 1, false, false, 0);
		BoardSpace property = new BoardSpace("test", "Property", 100, 10);
		player.purchaseProperty(property);
		Assertions.assertFalse(property.getOwner() == player);
	}

	@Test
	void testPurchaseProperty() {
		BoardSpace property = new BoardSpace("test", "Property", 100, 10);
		int beforeOwnedPropertiesAmount = player.getOwnedProperties().size();
		player.purchaseProperty(property);
		int afterOwnedPropertiesAmount = player.getOwnedProperties().size();
		Assertions.assertEquals(property.getOwner(), player);
		Assertions.assertTrue(afterOwnedPropertiesAmount - beforeOwnedPropertiesAmount == 1);
	}

}
