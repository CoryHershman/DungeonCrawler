
/*
 Programmer:Cory Hershman
 Class: Introduction to Networking
 Instructor: Mr. Wilson
 Assignment #: P0001
 Due Date: 2/23/2018
 Last Update: 2/22/2018
 Related Files: Dungeon.java, Character.java, Player.java, Monster.java
 Description: This class is used as the main class for the DungeonCrawler project. It runs the game.
 */

import java.util.Scanner;

public class DungeonCrawler {
  
  public static void main(String[] args) {

    Scanner scan = new Scanner(System.in); // Used to scan in user commands
    System.out.println("Server or Client?");
    String input = scan.nextLine();
    if(input.equalsIgnoreCase("Server")) {
      Server server = new Server();
      server.run();
    } else if(input.equalsIgnoreCase("Client")) {
      Client client = new Client();
      client.run();
    }
    
  }

  public static void start() {

    // Exposition for the player to read before playing
    System.out.println("You are an adventurer, "
        + "you are on a quest to reach the top of the tower to banish the demon "
        + "that has been terrorizing the nearby civilians.\n");
    System.out.println("The tower has 100 floors.\n");
    System.out.println("To your surprise, the demon appears on the first floor of the tower. "
        + "You fight and vanquish him.\n");
    System.out.println("However, to your dismay the demon reappears on the second floor. "
        + "He continues to reappear on each and every floor of the tower.\n");
    System.out.println("This long and grueling battle will test your perserverance. "
        + "As you fight, you will become stronger, but the demon will aswell.\n");
    System.out.println("You are continuing the fight starting from the tenth floor.\n");
    System.out.println("You will start the dungeon as a level 10 character. "
        + "Choose which stats you want to level up for the first 10 levels");
    System.out.println("HP determines your maximum health");
    System.out.println("Strength determines your attack damage");
    System.out.println("Dexterity determines your chance of hitting");
    System.out.println("Endurance determines the amount of health you will heal when healing");
    System.out.println("Crit Chance is the chance that you will deal 4x damage when attacking");
    System.out.println("Tip: There is no need to ever have a dexterity above 17 "
        + "or a crit chance above 100\n");

    
  }
  
  public static void levelUp() {
    System.out.println("Your current stats:");
    System.out.println("Level: " + 1);
    System.out.println("HP: " + 2);
    System.out.println("Strength: " + 1);
    System.out.println("Dexterity: " + 1);
    System.out.println("Endurance: " + 1);
    System.out.println("Crit Chance: " + 5 + "%");
    System.out.println("\n\nChoose a stat to level up by typing its name, "
        + "such as \"strength\" or \"HP\"");
  }
}
