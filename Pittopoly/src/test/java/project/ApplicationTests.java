package project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	Application app = new Application();

	@Test
	void exampleTest() {
		Assertions.assertEquals("Not yet implemented.", app.example());
	}

}
