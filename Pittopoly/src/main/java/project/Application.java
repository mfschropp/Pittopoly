package project;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("!test")
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.run(args);
	}

	@Override
	public void run(String[] args) {
		Scanner scan = new Scanner(System.in);
		Game game = new Game(scan);
		game.menu();
		/*System.out.println("Welcome to Pittopoly, what would you like to do?");
		System.out.println("[1]: Start New Game");
		System.out.println("[2]: Load Game");
		System.out.println("[3]: Exit Game");

		Scanner scan = new Scanner(System.in);
		int s = scan.nextInt();

		switch(s) {
			case 1:
				game.initialize();
				game.start();
				break;
			default:
				exit(0);
		}*/

	}

	/*public void exit(int n) {
		if (n == 0) {
			System.out.println("Unable to start game. Exiting...");
			System.exit(0);
		}
	}*/

}
