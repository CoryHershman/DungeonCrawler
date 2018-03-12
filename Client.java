
/*
 Programmer: Cory Hershman
 Class: Introduction to Networking
 Instructor: Mr. Wilson
 Assignment #: P0001
 Due Date: 2/23/2018
 Last Update: 2/22/2018
 Related Files: Dungeon.java, Character.java, Player.java, Monster.java, Server.java
 Description: This class creates a Client object that allows the user to send commands to the Server
 Interfaces with the Server class to send and receive packets.
 */

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

  private DatagramSocket socket; // socket that will receive packets
  private DatagramPacket packet; // packet that will be used by the socket
  private InetAddress address; // will hold the address of the client
  private byte buf[] = new byte[256]; // buffer for the packet
  private final int CLIENT_PORT = 2017; // the port the client will use
  private final int SERVER_PORT = 2018; // the port the server will use
  int playerLevel = 0;

  public Client() {
    try {
      socket = new DatagramSocket(CLIENT_PORT); // create a socket
      address = InetAddress.getByName("localhost"); // need to find out how to get Server's IP
    } catch (SocketException e) {
      System.err.println("Was not able to create a socket on port " + CLIENT_PORT);
      e.printStackTrace();
    } catch (UnknownHostException e) {
      System.err.println("Was not able to find the host");
      e.printStackTrace();
    }
  }

  // run method
  // Requires: Nothing
  // Returns: Nothing
  // The run method acts like a main method for this thread/class.
  public void run() {
    try {

      packet = new DatagramPacket(buf, buf.length, address, SERVER_PORT);
      socket.send(packet); // send a packet to the server to give it the client's address and port

      DungeonCrawler.start(); // Exposition for the user to read

      // for loop runs 10 times for 10 level ups
      for (int i = 0; i < 10; i++) {
        sendUpgrade(); // sends upgrade choice to the server
        playerLevel++;
      }

      receiveState(); // receive and print out the game state

      System.out.println("You control your hero by typing in commands. \nThere are three commands: "
          + "\"right\" to move right, \"attack\" to attack, \"heal\" to heal wounds. "
          + "\nIt is also acceptable to type either \"r\" \"a\" or \"h\".\n");

      while (true) {
        sendAction(); // send action choice to the server

        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet); // receive message from server

        if (buf[0] == 1) { // if monster dies
          System.out.println(
              "You have defeated the demon..... for now. You rest a bit and head up to the next floor\n");
          sendUpgrade(); // send upgrade choice to the server
          playerLevel++;
        } else if (buf[0] == 2) { // if player dies
          System.out.println("The monster has defeated you."
              + "\nYour valiant journey has come to an end.\nYou made it to floor " + playerLevel
              + "\nBetter luck next time.");
          close(); // close the socket
          System.exit(0); // end the client
        }

        receiveState(); // receive the game state from the server

        buf = new byte[256]; // reset the buffer's size
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet); // receive the hp totals from the server
        String hpTotals = new String(packet.getData());
        System.out.println(hpTotals);

      }

    } catch (IOException e) {
      System.err.println("IOException thrown: problem receiving or sending packet");
      e.printStackTrace();
    }

  }

  // sendAction method
  // Requires: Nothing
  // Returns: Nothing
  // Sends the user's action choice to the server
  public void sendAction() throws IOException {
    buf = new byte[256]; // reset the buffer's size
    Scanner scan = new Scanner(System.in);
    String input;

    while (true) {
      input = scan.nextLine();
      buf = input.getBytes();
      packet = new DatagramPacket(buf, buf.length, address, SERVER_PORT);
      socket.send(packet); // send the user's action choice to the server

      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet); // receive confirmation from the server

      if (buf[0] == 1) { // if the input was not valid, re-prompt user
        System.out.println("That was not a valid input, type \"a\" \"h\" or \"r\" ");
      } else {
        break;
      }
    }

  }

  // sendUpgrade method
  // Requires: Nothing
  // Returns: Nothing
  // Sends the user's upgrade choice to the server
  public void sendUpgrade() throws IOException {
    buf = new byte[256]; // resets the buffer's size
    Scanner scan = new Scanner(System.in);
    String input;

    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet); // receives the player's stats
    String playerStats = new String(packet.getData());
    System.out.println(playerStats); // print the player's stats to the screen

    buf = new byte[256]; // resets the buffer's size

    while (true) {
      // client types in a stat to level up
      input = scan.nextLine();
      buf = input.getBytes();
      packet = new DatagramPacket(buf, buf.length, address, SERVER_PORT);
      socket.send(packet); // the chosen stat gets sent to server

      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet); // client waits to receive confirmation from server
      // if the received packet was an error message, redo client input
      if (buf[0] == 1) {
        System.out.println("\nYour choice was not valid, "
            + "type either \"HP\" \"Strength\" \"Dexterity\" \"Endurance\" or \"Crit Chance\"\n");
      } else {
        break;
      }
    }
    System.out.println("\nYou have leveled up your " + input + "\n");
  }

  // receiveState method
  // Requires: Nothing
  // Returns: Nothing
  // Receives the game state from the server
  public void receiveState() throws IOException {
    buf = new byte[256]; // resets the buffer's size
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet); // receive the game state from the server
    String gameState = new String(packet.getData());
    System.out.println(gameState);
  }

  // close method
  // Requires: Nothing
  // Returns: Nothing
  // Closes the socket
  public void close() {
    socket.close();
  }

}
