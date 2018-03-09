
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends Thread {
  
  private DatagramSocket socket; // socket that will receive packets 
  private byte[] buf = new byte[256]; // buffer for the packet
  private boolean running;
  
  public Server() {
    try {
    socket = new DatagramSocket(2018); // socket attached to designated port
    }catch(SocketException e) {
      System.err.println("Was not able to create a socket on port " + 2018);
      e.printStackTrace();
    }
  }
  
  public void run() {
    running = true;
    
    // currently running will always equal true
    while(running) {
      try {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet); // currently receiving an empty packet
        
        InetAddress address = packet.getAddress();
        int clientPort = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, clientPort);
        String received = new String(packet.getData(), 0, packet.getLength());
        
        System.out.print(received);
        // do something with received String
        
        socket.send(packet); // currently sending an empty packet
      } catch(IOException e) {
        System.err.println("IOException thrown: problem receiving or sending packet");
        e.printStackTrace();
      }
    }
  }
  
  public void close() {
    socket.close();
  }
  
}
