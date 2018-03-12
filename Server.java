
/*
 Programmer: Cory Hershman
 Class: Introduction to Networking
 Instructor: Mr. Wilson
 Assignment #: P0001
 Due Date: 3/31/2018
 Last Update: 3/31/2018
 Related Files: Dungeon.java, Character.java, Player.java, Monster.java, Client.java
 Description: This class creates a Server object that houses the logic of the DungeonCrawler game.
 Interfaces with the Client class to send and receive packet.
 */

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends Thread {

  private DatagramSocket socket; // socket that will receive packets
  private DatagramPacket packet; // packet that will be used by the socket
  InetAddress address; // will hold the address of the client
  int clientPort; // will hold the port number of the client
  private byte[] buf = new byte[256]; // buffer for the packet

  public Server() {
    try {
      socket = new DatagramSocket(2018); // socket attached to designated port
    } catch (SocketException e) {
      System.err.println("Was not able to create a socket on port " + 2018);
      e.printStackTrace();
    }
  }

  // run method
  // Requires: Nothing
  // Returns: Nothing
  // The run method acts like a main method for this thread/class.
  public void run() {

    Player player = new Player(); // create Player object
    Monster monster = new Monster(); // create Monster object
    Dungeon dungeon = new Dungeon(16, player, monster); // create dungeon object, size 16

    // set necessary links between objects
    player.setMonster(monster);
    player.setDungeon(dungeon);
    monster.setPlayer(player);
    monster.setDungeon(dungeon);

    try {
      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet); // receive a packet from client to make obtain information
      address = packet.getAddress(); // get the address of client's socket
      clientPort = packet.getPort(); // get the port of client's socket

      // for loop runs 10 times for 10 level ups
      for (int i = 0; i < 10; i++) {
        receiveUpgrade(player);
      }
      // the following assignments to address and clientPort seem unneeded, but
      // necessary to compile
      address = packet.getAddress();
      clientPort = packet.getPort();
      // monster levels up 5 times
      monster.levelUp();
      monster.levelUp();
      monster.levelUp();
      monster.levelUp();
      monster.levelUp();

      dungeon.nextFloor(); // nextFloor method is called to start the first floor
      sendState(dungeon.drawDungeon()); // send the game state to the client

      // start of the game loop
      while (true) {
        receiveAction(player); // wait for client to choose their action
        if (monster.hitPoints > 0) { // if the monster is still alive
          monster.moveLeft(); // monster moves left
          monster.attack(); // monster attacks player
          if (player.hitPoints > 0) { // if the player is also alive
            buf[0] = 0; // message says the monster lives, and the player lives
          } else { // if the player is not alive
            buf[0] = 2; // message says the player died
          }
        } else { // if the monster is dead
          monster.levelUp(); // level up the monster
          buf[0] = 1; // message says the monster died
        }
        packet = new DatagramPacket(buf, buf.length, address, clientPort); // outgoing packet
        socket.send(packet); // send packet with the message
        if (buf[0] == 1) { // if the monster died
          receiveUpgrade(player); // wait for client to choose their upgrade
        } else if (buf[0] == 2) { // if the player died
          close();
          System.exit(0); // end the system
        }
        sendState(dungeon.drawDungeon()); // send the game state to the client

        String hpTotals = ("Your HP: " + player.hitPoints + "     Monsters HP: "
            + monster.hitPoints);
        buf = hpTotals.getBytes();
        packet = new DatagramPacket(buf, buf.length, address, clientPort); // outgoing packet
        socket.send(packet); // send the hp totals of the player and monster to the client
      } // end of the game loop

    } catch (IOException e) {
      System.err.println("IOException thrown: problem receiving or sending packet");
      e.printStackTrace();
    }
  }

  // receiveAction method
  // Requires: The Player object
  // Returns: Nothing
  // Server waits for the client to choose their action. Tells the client if the action was valid.
  public void receiveAction(Player player) throws IOException {
    buf = new byte[256]; // reset the size of the buffer
    Boolean validInput;

    do {
      validInput = true;
      packet = new DatagramPacket(buf, buf.length); // incoming packet
      socket.receive(packet); // receive the clients action choice
      String received = new String(packet.getData(), 0, packet.getLength());

      if (received.equalsIgnoreCase("right") || received.equalsIgnoreCase("r")) {
        player.moveRight();
      } else if (received.equalsIgnoreCase("attack") || received.equalsIgnoreCase("a")) {
        player.attack();
      } else if (received.equalsIgnoreCase("heal") || received.equalsIgnoreCase("h")) {
        player.heal();
      } else {
        validInput = false;
      }

      if (validInput == false) { // if client's input is not valid
        buf[0] = 1; // send error message
      } else { // if client's input is valid
        buf[0] = 0; // send confirmation message
      }
      packet = new DatagramPacket(buf, buf.length, address, clientPort);
      socket.send(packet); // send message
    } while (!validInput); // if the input was not valid, re-prompt client

  }

  // receiveUpgrade method
  // Requires: The Player object
  // Returns: Nothing
  // Waits for the client to choose their upgrade for their character. Calls the player.levelUp
  // method to handle the logic. Tells the client if their choice was valid.
  public void receiveUpgrade(Player player) throws IOException {
    buf = new byte[256]; // reset buffer's size
    Boolean validInput;

    String playerStats = "Your current stats:";
    playerStats = playerStats + "\nLevel: " + player.level;
    playerStats = playerStats + "\nHP: " + player.maxHitPoints;
    playerStats = playerStats + "\nStrength: " + player.strength;
    playerStats = playerStats + "\nDexterity: " + player.dexterity;
    playerStats = playerStats + "\nEndurance: " + player.endurance;
    playerStats = playerStats + "\nCrit Chance: " + player.critChance;
    playerStats = playerStats + "\n\nChoose a stat to level up by typing its name, "
        + "such as \"strength\" or \"HP\"";

    buf = playerStats.getBytes();
    packet = new DatagramPacket(buf, buf.length, address, clientPort);
    socket.send(packet); // send playerStats to the client

    buf = new byte[256]; // reset buffer's size

    do {
      validInput = true;
      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet); // server receives client's chosen stat

      String received = new String(packet.getData(), 0, packet.getLength());
      validInput = player.levelUp(received); // client's chosen stat sent to levelUp method

      if (validInput == false) {
        buf[0] = 1; // if the client's input was non-valid, send error message
      } else {
        buf[0] = 0; // else, send confirmation message
      }
      packet = new DatagramPacket(buf, buf.length, address, clientPort);
      socket.send(packet); // send packet to client with error or confirmation message
    } while (!validInput); // if the client's input was not valid, re-prompt client
  }

  // sendState method
  // Requires: A String that represents the game state
  // Returns: Nothing
  // Sends the representation of the game state to the client
  public void sendState(String gameState) throws IOException {
    buf = gameState.getBytes();
    packet = new DatagramPacket(buf, buf.length, address, clientPort);
    socket.send(packet); // send the game state to the client
    buf = new byte[256]; // reset the buffer's size
  }

  // close method
  // Requires: Nothing
  // Returns: Nothing
  // Closes the socket
  public void close() {
    socket.close();
  }

}
