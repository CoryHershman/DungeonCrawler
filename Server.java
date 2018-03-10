
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends Thread {

  private DatagramSocket socket; // socket that will receive packets
  private DatagramPacket packet;
  InetAddress address;
  int clientPort;
  private byte[] buf = new byte[256]; // buffer for the packet

  public Server() {
    try {
      socket = new DatagramSocket(2018); // socket attached to designated port
    } catch (SocketException e) {
      System.err.println("Was not able to create a socket on port " + 2018);
      e.printStackTrace();
    }
  }

  public void run() {

    Player player = new Player();
    Monster monster = new Monster();
    Dungeon dungeon = new Dungeon(16, player, monster);
    //Boolean validInput;

    try {
      // for loop runs 10 times for 10 level ups
      for (int i = 0; i < 10; i++) {
        receiveUpgradeChoice(player);
      }
      // the following assignments to address and clientPort seem unneeded, but necessary to compile
      address = packet.getAddress();
      clientPort = packet.getPort();
      // monster levels up 5 times
      monster.levelUp();
      monster.levelUp();
      monster.levelUp();
      monster.levelUp();
      monster.levelUp();
      
      // nextFloor method is called to start the first floor
      dungeon.nextFloor();
      sendGameState(dungeon.drawDungeon());
        
      // this is the original code from DungeonCrawler for the main game loop
      // implement this as server-client
      /*
      Scanner scan = new Scanner(System.in);
      
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
      */

      
    } catch (IOException e) {
      System.err.println("IOException thrown: problem receiving or sending packet");
      e.printStackTrace();
    }

  }
  
  public void receiveUpgradeChoice(Player player) throws IOException {
    Boolean validInput;
    buf = new byte[256];
    
    do {
      validInput = true;
      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet); // server receives client's chosen stat
      address = packet.getAddress(); // learned client's address
      clientPort = packet.getPort(); // learned client's port
      
      String received = new String(packet.getData(), 0, packet.getLength());
      validInput = player.levelUp(received); // client's chosen stat sent to levelUp method
      
      if(validInput == false) {
        buf[0] = 1; // if the client's input was non-valid, send error message
      } else {
        buf[0] = 0; // else, send confirmation message
      }
      packet = new DatagramPacket(buf, buf.length, address, clientPort);
      socket.send(packet); // send packet to client with error or confirmation message
    } while (!validInput);
  }
  
  public void sendGameState(String gameState) throws IOException {
    buf = gameState.getBytes();
    packet = new DatagramPacket(buf, buf.length, address, clientPort);
    socket.send(packet);
    buf = new byte[256];
  }

  public void close() {
    socket.close();
  }

}
