
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

      // nextFloor method is called to start the first floor
      dungeon.nextFloor();
      sendState(dungeon.drawDungeon());

      while (true) {
        receiveAction(player);
        if (monster.hitPoints > 0) {
          monster.moveLeft();
          monster.attack();
          if (player.hitPoints > 0) {
            buf[0] = 0; // monster lives, player lives
          } else {
            buf[0] = 2; // player dies
          }
        } else {
          monster.levelUp();
          buf[0] = 1; // monster dies
        }
        packet = new DatagramPacket(buf, buf.length, address, clientPort);
        socket.send(packet);
        if (buf[0] == 1) {
          receiveUpgrade(player);
        } else if (buf[0] == 2) {
          System.exit(0);
        }
        sendState(dungeon.drawDungeon());

        String hpTotals = ("Your HP: " + player.hitPoints + "     Monsters HP: "
            + monster.hitPoints);
        buf = hpTotals.getBytes();
        packet = new DatagramPacket(buf, buf.length, address, clientPort);
        socket.send(packet);
      }

    } catch (IOException e) {
      System.err.println("IOException thrown: problem receiving or sending packet");
      e.printStackTrace();
    }

  }

  public void receiveAction(Player player) throws IOException {
    buf = new byte[256];
    Boolean validInput;

    do {
      validInput = true;
      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);

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

      if (validInput == false) {
        buf[0] = 1;
      } else {
        buf[0] = 0;
      }
      packet = new DatagramPacket(buf, buf.length, address, clientPort);
      socket.send(packet);
    } while (!validInput);

  }

  public void receiveUpgrade(Player player) throws IOException {
    buf = new byte[256];
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
    socket.send(packet);
    
    buf = new byte[256];
    
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
    } while (!validInput);
  }

  public void sendState(String gameState) throws IOException {
    buf = gameState.getBytes();
    packet = new DatagramPacket(buf, buf.length, address, clientPort);
    socket.send(packet);
    buf = new byte[256];
  }

  public void close() {
    socket.close();
  }

}
