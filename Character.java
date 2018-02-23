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
	
	Dungeon dungeon;						//A link to the hosting dungeon, for the purpose of calling methods from it
	
	//Instance variables for the Character object
	int level;								//The character's level
	int hitPoints;							//The character's current number of hit points
	int maxHitPoints;						//The character's maximum hit points currently
	int strength;							//The character's strength, determines damage done when attacking
	int dexterity;							//The character's dexterity, determines hit chance when attacking
	int hitChance;							//The character's hit chance when attacking, determined by dexterity
	int position;							//The character's position in the dungeon
	
	//Abstract methods to be initialized by inheriting methods
	
	abstract void setDungeon(Dungeon dungeon);				//setDungeon method is used to set up the link to the hosting dungeon
	
	abstract void levelUp();								//levelUp method is used to level up the character's stats
	
	abstract void attack();									//attack method is used for attacking other characters
	
	abstract void defeated();								//defeated method is used when the character is defeated
	
	//getPosition method
	//Requires: Nothing
	//Returns: The position of the character as an int
	public int getPosition() {
		return position;
	}
	
	//setPosition method
	//Requires: The new position of the character as an int
	//Returns: Nothing
	public void setPosition(int i) {
		position = i;
	}
}
