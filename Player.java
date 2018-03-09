
/*
 Programmer:Cory Hershman
 Class: Introduction to Networking
 Instructor: Mr. Wilson
 Assignment #: P0001
 Due Date: 2/23/2018
 Last Update: 2/22/2018
 Related Files: Dungeon.java, Character.java, Monster.java, DungeonCrawler.java
 Description: This class is used to create a Player object for the user to control. It inherits the Character.java abstract class.
 */

import java.util.Scanner;
import java.util.Random;

public class Player extends Character {

  Monster monster; // A link to the monster, for the purpose of calling methods from it
  int critChance; // Critical hit chance when attacking
  int endurance; // Determines the amount healed when healing
  Random rand = new Random(); // Psuedo-random number generator

  // Player constructor
  // Requires: Nothing
  public Player() {
    level = 0; // Player starts at level 0
    maxHitPoints = 2; // maxHitPoints start at 2
    hitPoints = 2; // hitPoints start at 2
    strength = 1; // strength starts at 1
    dexterity = 1; // dexterity starts at 1
    endurance = 1; // endurance starts at 1
    critChance = 5; // critChance starts at 5
    hitChance = 20; // hitChance starts at 20
  }

  // moveRight method
  // Requires: Nothing
  // Returns: The new position of the character as an int
  // This method moves the player right
  public int moveRight() {
    if (monster.getPosition() != position + 1) { // If monster is not in front of player
      position = position + 1; // Increase player's position by 1
      System.out.println("Player has moved right");
    } else // If monster is in front of player
      System.out.println("Cannot move right"); // Player does not move
    return position; // return the new position
  }

  // setDungeon method
  // Requires: The Dungeon Object of the game
  // Returns: Nothing
  // This method sets a link to the Dungeon
  public void setDungeon(Dungeon dungeon) {
    this.dungeon = dungeon;
  }

  // setMonster method
  // Requires: The Monster Object of the game
  // Returns: Nothing
  // This method sets a link to the Monster
  public void setMonster(Monster monster) {
    this.monster = monster;
  }

  // attack method
  // Requires: Nothing
  // Returns: Nothing
  // This method is used when the player wants to attack the monster
  public void attack() {
    // If monster is within range, try to hit monster
    if (dungeon.inRange) {
      int accuracy = rand.nextInt(100) + 1; // accuracy between 1-100
      if (accuracy <= hitChance) {
        int effort = rand.nextInt(100) + 1; // effort between 1-100
        if (effort <= critChance) {
          monster.hitPoints = monster.hitPoints - (4 * strength); // monster takes 4x normal damage
          System.out
              .println("\nYou have dealt a critical hit and dealt " + (4 * strength) + " damage!");
        } else { // If effort is greater than critChance
          monster.hitPoints = monster.hitPoints - strength; // monster takes normal damage
          System.out.println("\nYou have wounded the demon and dealt " + strength + " damage");
        }
        if (monster.hitPoints <= 0) { // If monster's hitPoints are 0 or less
          monster.defeated(); // monster is defeated
        }
      } else // If accuracy is greater than hitChance
        System.out.println("\nYou have missed"); // attack misses
    } else // If monster is not within range, attack misses
      System.out.println("You have missed, the monster is not within range");
  }

  // heal method
  // Requires: Nothing
  // Returns: Nothing
  // This method is used when the player wants to heal
  public void heal() {
    hitPoints = hitPoints + endurance; // player heals an amount of hit points equal to endurance
    if (hitPoints > maxHitPoints) { // If hit points are higher than max hit points
      hitPoints = maxHitPoints; // Hit points become equal to max hit points
    }
  }

  // defeated method
  // Requires: Nothing
  // Returns: Nothing
  // This method is used when the player is defeated. The game ends.
  public void defeated() {
    System.out.println("The monster has defeated you."
        + "\nYour valiant journey has come to an end.\nYou made it to floor " + level
        + "\nBetter luck next time.");
    System.exit(0); // program ends
  }

  // levelUp method
  // Requires: Nothing
  // Returns: Nothing
  // This method is used when the player levels up. The player chooses which stat
  // they wish to increase.
  public Boolean levelUp(String input) {

    if (input.equalsIgnoreCase("HP")) { // If user inputs "hp"
      maxHitPoints = maxHitPoints + 2; // Increase maxHitPoints by 2
      //System.out.println("Your HP has been increased to " + maxHitPoints + "\n");
    } else if (input.equalsIgnoreCase("Strength")) { // If user inputs "strength"
      strength = strength + 1; // Increase strength by 1
      //System.out.println("Your Strength has been increased to " + strength + "\n");
    } else if (input.equalsIgnoreCase("Dexterity")) { // If user inputs "dexterity"
      dexterity = dexterity + 1; // Increase dexterity by 1
      hitChance = hitChance + 5; // Increase hitChance by 5
      //System.out.println("Your Dexterity has been increased to " + dexterity + "\n");
    } else if (input.equalsIgnoreCase("Endurance")) { // If user inputs "endurance"
      endurance = endurance + 1; // Increase endurance by 1
      //System.out.println("Your Endurance has been increased to " + endurance + "\n");
    } else if (input.equalsIgnoreCase("Crit Chance") || input.equalsIgnoreCase("CritChance")) {
      critChance = critChance + 5; // Increase critChance by 5
      //System.out.println("Your Crit Chance has been increased to " + critChance + "\n");
    } else { // If user inputs a non-valid response
      //System.out.println("Your choice was not valid, "
      //    + "type either \"HP\" \"Strength\" \"Dexterity\" \"Endurance\" or \"Crit Chance\"");
      return false; // client input a non-valid response, re-prompt client
    }
    level = level + 1; // Increase player's level by 1
    hitPoints = maxHitPoints; // hit points become equal to max hit points
    return true;
  }
}
