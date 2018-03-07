
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
    Player player = new Player(); // Instantiate player Object
    Monster monster = new Monster(); // Instantiate monster Object
    Dungeon dungeon = new Dungeon(16, player, monster); // Size 16, linked to the player and monster
    player.setDungeon(dungeon); // Link to the dungeon Object
    player.setMonster(monster); // Link to the monster Object
    monster.setDungeon(dungeon); // Link to the dungeon Object
    monster.setPlayer(player); // Link to the player Object

    setup(player, monster, dungeon); // Setup method to start of the game

    Scanner scan = new Scanner(System.in); // Used to scan in user commands

    // Infinite while loop that will continue until the game ends
    while (true) {
      String input = scan.nextLine(); // Holds the user's response
      if (input.equalsIgnoreCase("right") || input.equalsIgnoreCase("r")) {
        player.moveRight(); // Call the moveRight method
      } else if (input.equalsIgnoreCase("attack") || input.equalsIgnoreCase("a")) {
        player.attack(); // Call the attack method
      } else if (input.equalsIgnoreCase("heal") || input.equalsIgnoreCase("h")) {
        player.heal(); // Call the heal method
      } else { // If user enters a non-valid response
        System.out.println("That was not a valid input, try again");
        continue; // Restart the while loop, asking for the user's response again
      }
      monster.moveLeft(); // The monster moves left
      monster.attack(); // The monster tries to attack
      dungeon.drawDungeon(); // The dungeon is redrawn
      System.out.println("Your HP: " + player.hitPoints + "     Monsters HP: " + monster.hitPoints);
    }
  }

  public static void setup(Player player, Monster monster, Dungeon dungeon) {

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

    // The player levels up 9 times
    player.levelUp();
    player.levelUp();
    player.levelUp();
    player.levelUp();
    player.levelUp();
    player.levelUp();
    player.levelUp();
    player.levelUp();
    player.levelUp();

    // The monster levels up 4 times
    monster.levelUp();
    monster.levelUp();
    monster.levelUp();
    monster.levelUp();

    // nextFloor method is called to start the first floor. Both the player and
    // monster level up 1 more time
    dungeon.nextFloor();
    System.out.println("You control your hero by typing in commands. \nThere are three commands: "
        + "\"right\" to move right, \"attack\" to attack, \"heal\" to heal wounds. "
        + "\nIt is also acceptable to type either \"r\" \"a\" or \"h\".\n");
  }
}
