
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
      address = InetAddress.getByName("localhost");
    } catch (SocketException e) {
      System.err.println("Was not able to create a socket on port " + CLIENT_PORT);
      e.printStackTrace();
    } catch (UnknownHostException e) {
      System.err.println("Was not able to find the host");
      e.printStackTrace();
    }
  }

  public void run() {
    Scanner scan = new Scanner(System.in);
    String input;

    try {

      DungeonCrawler.start();
      
      // for loop runs 9 times for 9 level ups
      for (int i = 0; i < 9; i++) {
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

    } catch (IOException e) {
      System.err.println("IOException thrown: problem receiving or sending packet");
      e.printStackTrace();
    }

  }

  public void close() {
    socket.close();
  }

}
