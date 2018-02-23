/*
 Programmer:Cory Hershman
 Class: Introduction to Networking
 Instructor: Mr. Wilson
 Assignment #: P0001
 Due Date: 2/23/2018
 Last Update: 2/22/2018
 Related Files: Dungeon.java, Character.java, Monster.java, DungeonCrawler.java
 Description: This class is used to create a Player object for the user to control. It inherites the Character.java abstract class.
 */

import java.util.Scanner;
import java.util.Random;

public class Player extends Character {
	
	Monster monster;					//A link to the monster, for the purpose of calling methods from it
	int critChance;						//The player's critical hit chance when attacking
	int endurance;						//The player's endurance, determines the amount of hit points healed when healing
	Random rand = new Random();			//A Random Object, used for generating random numbers
	
	//Player constructor
	//Requires: Nothing
	public Player() {
		level = 0;						//Player starts at level 0
		maxHitPoints = 2;				//maxHitPoints start at 2 and increase by 2 each level
		hitPoints = 2;					//hitPoints start at 2
		strength = 1;					//strength starts at 1 and increases by 1 each level
		dexterity = 1;					//dexterity starts at 1 and increases by 1 each level
		endurance = 1;					//endurance starts at 1 and increases by 1 each level
		critChance = 3;					//critChance starts at 3 and increases by 3 each level
		hitChance = 35;					//hitChance starts at 35 and increases by 3 for each level in dexterity
	}
	
	//moveRight method
	//Requires: Nothing
	//Returns: The new position of the character as an int
	public int moveRight() {
		if(monster.getPosition() != position+1 && position != dungeon.size - 1) {	//If there is no monster or wall in front of the player
			position = position+1;													//Then move the player's position rightward by 1
			System.out.println("Player has moved right");
		}
		else																		//If there is a monster or wall in front of the player
			System.out.println("Cannot move right");								//Player does not move
		return position;															//return the new position
	}
	
	//setDungeon method
	//Requires: The Dungeon Object of the game
	//Returns: Nothing
	//This method sets a link to the Dungeon
	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	//setMonster method
	//Requires: The Monster Object of the game
	//Returns: Nothing
	//This method sets a link to the Monster
	public void setMonster(Monster monster) {
		this.monster = monster;
	}
	
	//attack method
	//Requires: Nothing
	//Returns: Nothing
	//This method is used when the player wants to attack the monster
	public void attack() {
		if(dungeon.inRange) {									//If the monster is within range, try to hit it
			int accuracy = rand.nextInt(100) + 1;					//Accuracy between 1-100
			if(accuracy <= hitChance) {								//If accuracy is less than hitChance, the player has hit the monster
				int effort = rand.nextInt(100) + 1;						//Effort between 1-100
				if(effort <= critChance) {								//if effort is less than critChance, then a critical hit has been inflicted
					monster.hitPoints = monster.hitPoints - (4*strength);	//The monster takes 4x normal damage
					System.out.println("\nYou have dealt a critical hit and dealt " + (4*strength) + " damage!");
				}
				else {													//If effort is greater than critChance, then a normal hit has been inflicted
					monster.hitPoints = monster.hitPoints - strength;		//The monster takes damage equal to the player's strength
					System.out.println("\nYou have wounded the monster and dealt " + strength + " damage");
				}
				if(monster.hitPoints <= 0)								//If the monster's hitPoints are 0 or less, then it has been defeated
					monster.defeated();
			}
			else													//If accuracy is greater than hitChance, the attack misses
				System.out.println("\nYou have missed");			
		}
		else													//If the monster is not within range, the attack misses
			System.out.println("You have missed, the monster is not within range");
	}
	
	//heal method
	//Requires: Nothing
	//Returns: Nothing
	//This method is used when the player wants to heal
	public void heal() {
		hitPoints = hitPoints + endurance;				//The player heals an amount of hit points equal to endurance
		if(hitPoints > maxHitPoints)					//If the player's hit points are higher than maximum hit points
			hitPoints = maxHitPoints;						//Then the player's hit points become equal to maximum hit points
	}
	
	//defeated method
	//Requires: Nothing
	//Returns: Nothing
	//This method is used when the player is defeated. The game ends.
	public void defeated() {
		System.out.println("The monster has defeated you.\nYour valiant journey has come to an end.\nYou made it to level " + level + "\nBetter luck next time.");
		System.exit(0);
	}
	
	//levelUp method
	//Requires: Nothing
	//Returns: Nothing
	//This method is used when the player levels up. The player chooses which stat they wish to increase.
	public void levelUp() {
		Scanner scan = new Scanner(System.in);						//scan will be used to scan in the player's response
		String input;												//input will be used to hold the player's response
		Boolean validChoice;										//validChoice will be used to re-prompt the user if they input a non-valid response
		
		System.out.println("Your current stats:");
		System.out.println("Level: " + level);
		System.out.println("HP: " + maxHitPoints);
		System.out.println("Strength: " + strength);
		System.out.println("Dexterity: " + dexterity);
		System.out.println("Endurance: " + endurance);
		System.out.println("Crit Chance: " + critChance + "%");
		System.out.println("\n\nChoose a stat to level up by typing its name, such as \"strength\" or \"HP\"");
		
		//This do-while loop iterates through asking the player for a stat choice, incase they enter a non-valid response
		do{
			validChoice = true;										//validChoice set to true
			input = scan.nextLine();								//input holds the player's response
			if(input.equalsIgnoreCase("HP")) {						//If the player inputs "hp"
				maxHitPoints = maxHitPoints + 2;						//Then the maxHitPoints stat goes up by 2
				System.out.println("Your HP has been increased to " + maxHitPoints + "\n");
			}
			else if(input.equalsIgnoreCase("Strength")) {			//If the player inputs "strength"
				strength = strength + 1;								//Then the strength stat goes up by 1
				System.out.println("Your Strength has been increased to " + strength + "\n");
			}
			else if(input.equalsIgnoreCase("Dexterity")) {			//If the player inputs "dexterity"
				dexterity = dexterity + 1;								//Then the dexterity stat goes up by 1
				hitChance = hitChance + 3;								//And their hitChance goes up by 3%
				System.out.println("Your Dexterity has been increased to " + dexterity + "\n");
			}
			else if(input.equalsIgnoreCase("Endurance")) {			//If the player inputs "endurance
				endurance = endurance + 1;								//Then the endurance stat goes up by 1
				System.out.println("Your Endurance has been increased to " + endurance + "\n");
			}
			else if(input.equalsIgnoreCase("Crit Chance") || input.equalsIgnoreCase("CritChance")) {	//If the player inputs "crit chance"
				critChance = critChance + 3;																//Then the critChance stat goes up by 3%
				System.out.println("Your Crit Chance has been increased to " + critChance + "\n");
			}
			else {													//If the player inputs a non-valid response
				System.out.println("Your choice was not valid, type either \"HP\" \"Strength\" \"Dexterity\" \"Endurance\" or \"Crit Chance\"");
				validChoice = false;									//Set validChoice to false
			}
		}while(!validChoice);										//Iterate again if the choice was non-valid; otherwise, continue
		level = level + 1;										//The player's level increases by 1
		hitPoints = maxHitPoints;								//The player's hit points become equal with their maximum hit points
	}
}