
/*
 Programmer:Cory Hershman
 Class: Introduction to Networking
 Instructor: Mr. Wilson
 Assignment #: P0001
 Due Date: 2/23/2018
 Last Update: 2/22/2018
 Related Files: Dungeon.java, Character.java, Player.java, DungeonCrawler.java
 Description: This class is used to create a Monster object for the user to fight. It inherites the Character.java abstract class.
 */

import java.util.Random;

public class Monster extends Character {

  Player player; // A link to the player, for the purpose of calling methods from it
  Dungeon dungeon;
  Random rand = new Random(); // A Random Object, used for generating random numbers

  // Monster constructor
  // Requires: Nothing
  public Monster() {
    level = 0; // Monster starts at level 0
    maxHitPoints = 2; // maxHitPoints start at 2
    hitPoints = 2; // hitPoints start at 2
    strength = 1; // strength starts at 1
    dexterity = 1; // dexterity starts at 1
    hitChance = 20; // hitChance starts at 20
  }

  // moveLeft method
  // Requires: Nothing
  // Returns: The new position of the character
  public int moveLeft() {
    if (player.getPosition() != position - 1) { // If player is not in front of monster
      position = position - 1; // Decrease monster's position by 1
      System.out.println("Monster has moved left");
    }
    return position; // return the new position
  }

  // setDungeon method
  // Requires: The Dungeon Object of the game
  // Returns: Nothing
  // This method sets a link to the Dungeon
  public void setDungeon(Dungeon dungeon) {
    this.dungeon = dungeon;
  }

  // setPlayer method
  // Requires: The Player Object of the game
  // Returns: Nothing
  // This method sets a link to the Player
  public void setPlayer(Player player) {
    this.player = player;
  }

  // attack method
  // Requires: Nothing
  // Returns: Nothing
  // This method is used when the monster tries to attack the player
  public void attack() {
    // If player is within range, try to hit player
    if (dungeon.inRange) {
      int accuracy = rand.nextInt(100) + 1; // accuracy between 1-100
      if (accuracy <= hitChance) { // If accuracy is less than hitChance
        player.hitPoints = player.hitPoints - strength; // Then do damage to player equal to
                                                        // monster's strength
        System.out.println("The monster has wounded you and dealt " + strength + " damage");
        if (player.hitPoints <= 0) { // If the player's hit points are 0 or less
          player.defeated(); // Then the player is defeated
        }
      }
    } // If the player is not within range, the attack does nothing
  }

  // defeated method
  // Requires: Nothing
  // Returns: Nothing
  // This method is used when the monster is defeated. The floor is beaten and the
  // next floor begins
  public void defeated() {
    System.out.println(
        "You have defeated the demon..... for now. You rest a bit and head up to the next floor\n");
    dungeon.nextFloor(); // Begin the next floor of the dungeon
  }

  // levelUp method
  // Requires: Nothing
  // Returns: Nothing
  // This method is used when the monster levels up. The monster chooses which
  // stat it wishes to increase at random.
  public void levelUp() {
    int r = rand.nextInt(3); // r is used to determine which stat will be increased
    if (r == 0) {
      maxHitPoints = maxHitPoints + 2; // Increase maxHitPoints by 2
    }
    if (r == 1) {
      strength = strength + 1; // Increase strength by 1
    }
    if (r == 2) { // If r = 2
      dexterity = dexterity + 1; // Increase dexterity by 1
      hitChance = hitChance + 5; // Increase hitChance by 5
    }
    level = level + 1; // Increase monster's level by 1
    hitPoints = maxHitPoints; // Monster's hit points become equal to it's max hit points
  }
}
