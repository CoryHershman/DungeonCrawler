
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

  private DatagramSocket socket;
  private DatagramPacket packet;
  private InetAddress address;
  private byte buf[] = new byte[256];
  private final int CLIENT_PORT = 2017;

  public Client() {
    try {
      socket = new DatagramSocket(CLIENT_PORT);
      address = InetAddress.getByName("localhost"); // need to find out how to get Server's IP
    } catch (SocketException e) {
      System.err.println("Was not able to create a socket on port " + CLIENT_PORT);
      e.printStackTrace();
    } catch (UnknownHostException e) {
      System.err.println("Was not able to find the host");
      e.printStackTrace();
    }
  }

  public void run() {
    //Scanner scan = new Scanner(System.in);
    //String input;

    try {

      DungeonCrawler.start();
      
      // for loop runs 10 times for 10 level ups
      for (int i = 0; i < 10; i++) {
        sendUpgrade();
      }
      // receive and print out the game state
      receiveState();
      
      System.out.println("You control your hero by typing in commands. \nThere are three commands: "
          + "\"right\" to move right, \"attack\" to attack, \"heal\" to heal wounds. "
          + "\nIt is also acceptable to type either \"r\" \"a\" or \"h\".\n");
      
      while(true) {
        sendAction();
        
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        
        if(buf[0] == 1) {
          System.out.println(
              "You have defeated the demon..... for now. You rest a bit and head up to the next floor\n");
          sendUpgrade();
        }
        
        
        receiveState();
        
        buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String hpTotals = new String(packet.getData());
        System.out.println(hpTotals);
        
      }
      

    } catch (IOException e) {
      System.err.println("IOException thrown: problem receiving or sending packet");
      e.printStackTrace();
    }

  }
  
  public void sendAction() throws IOException {
    buf = new byte[256];
    Scanner scan = new Scanner(System.in);
    String input;
    
    while(true) {
      input = scan.nextLine();
      buf = input.getBytes();
      packet = new DatagramPacket(buf, buf.length, address, 2018);
      socket.send(packet);
      
      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);
      
      if(buf[0] == 1) {
        System.out.println("That was not a valid input, type \"a\" \"h\" or \"r\" ");
      } else {
        break;
      }
    }
    
  }

  public void sendUpgrade() throws IOException {
    buf = new byte[256];
    Scanner scan = new Scanner(System.in);
    String input;
    
    DungeonCrawler.levelUp();

    while(true) {
      // client types in a stat to level up
      input = scan.nextLine();
      buf = input.getBytes();
      packet = new DatagramPacket(buf, buf.length, address, 2018);
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
  
  public void receiveState() throws IOException {
    buf = new byte[256];
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);
    String gameState = new String(packet.getData());
    System.out.println(gameState);
  }
  
  public void close() {
    socket.close();
  }

}
