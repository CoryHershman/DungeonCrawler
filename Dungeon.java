/*
 Programmer:Cory Hershman
 Class: Introduction to Networking
 Instructor: Mr. Wilson
 Assignment #: P0001
 Due Date: 2/23/2018
 Last Update: 2/22/2018
 Related Files: Character.java, Player.java, Monster.java, DungeonCrawler.java
 Description: This class is used to create a dungeon object for the DungeonCrawler game
 */

public class Dungeon {

  Monster monster; // A link to the monster, for the purpose of calling methods from it
  Player player; // A link to the player, for the purpose of calling methods from it
  
  public boolean inRange; // True if player and monster are within attacking range of each other
  public int size;

  // Dungeon constructor
  // Requires: Size of the dungeon, instance of player, and instance of monster
  public Dungeon(int size, Player player, Monster monster) {
    this.size = size;
    this.player = player;
    this.monster = monster;
  }

  // drawDungeon method
  // Requires: Nothing
  // Returns: String that represents the state of the dungeon
  public String drawDungeon() {
    String gameState = "";
    gameState = gameState + "_";
    // For-loop draws the dungeon ceiling
    for (int i = 0; i < size; i++) {
      gameState = gameState + "_"; // Draws underscores to represent the ceiling
    }
    gameState = gameState + "_\n"; // Skips to next line
    gameState = gameState + "|"; // Draws the leftmost wall

    // For-loop draws the dungeon floor
    for (int i = 0; i < size; i++) {
      if (i == player.getPosition()) { // If the next space is the playerPosition
        gameState = gameState + "P"; // Draw P to represent the player's position
      } else if (i == monster.getPosition()) { // If the next space is the monsterPosition
        gameState = gameState + "M"; // Draw M to represent the monster's position
      } else { // If the next space does not contain a player or monster
        gameState = gameState + "_"; // Draws underscore to represent the floor
      }
    }
    gameState = gameState + "|\n"; // Draws the rightmost wall
    
    // If player and monster are next to each other, set inRange to true. If not, set inRange to false.
    if (player.getPosition() == monster.getPosition() - 1) {
      inRange = true;
    } else {
      inRange = false;
    }
    return gameState;
  }

  // nextFloor method
  // Requires: Nothing
  // Returns: Nothing
  // This method is used to progress the dungeon to the next floor. 
  // The character positions are reset and both characters level up
  public void nextFloor() {
    player.setPosition(0); // Player reset to position 0
    monster.setPosition(size - 1); // Monster reset to rightmost position
    if (player.level == 101) { // If player has finished the 100th floor, he/she wins the game
      System.out.println("Congratulations! You have made it to the top of the tower!"
          + "\nThe demon has been defeated and your valiant journey has come to an end.");
      System.out.println("Stories of you will be sung for many generations."
          + "\nThe great hero who climbed the tower and fought day and night against the demon.");
      System.exit(0); // The program ends
    }
  }
}
