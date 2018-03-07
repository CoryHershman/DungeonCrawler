/*
 Programmer:Cory Hershman
 Class: Introduction to Networking
 Instructor: Mr. Wilson
 Assignment #: P0001
 Due Date: 2/23/2018
 Last Update: 2/22/2018
 Related Files: Dungeon.java, Player.java, Monster.java, DungeonCrawler.java
 Description: This is an abstract class that can be inherited by character classes in a dungeon crawler
 */

abstract class Character {

  Dungeon dungeon; // Link to the hosting dungeon, for the purpose of calling methods from it

  // Instance variables for the Character object
  int level;
  int hitPoints; // Current number hit points
  int maxHitPoints; // maximum hit points
  int strength; // Determines damage done when attacking
  int dexterity; // Determines hit chance when attacking
  int hitChance; // Hit chance when attacking, determined by dexterity
  int position; // Character's position in the dungeon

  // Abstract methods to be initialized by inheriting classes

  abstract void setDungeon(Dungeon dungeon); // Used to set up the link to the hosting dungeon

  abstract void levelUp(); // Used to level up the character's stats

  abstract void attack(); // Used for attacking other characters

  abstract void defeated(); // Used when the character is defeated

  // getPosition method
  // Requires: Nothing
  // Returns: The position of the character
  public int getPosition() {
    return position;
  }

  // setPosition method
  // Requires: The new position of the character
  // Returns: Nothing
  public void setPosition(int i) {
    position = i;
  }
}
