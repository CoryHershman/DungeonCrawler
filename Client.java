
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends Thread{
  
  private DatagramSocket socket;
  private InetAddress address;
  private byte buf[] = new byte[256];
  private final int CLIENT_PORT = 2017;
  
  public Client() {
    try {
      socket = new DatagramSocket(CLIENT_PORT);
      address = InetAddress.getByName("localhost");
    } catch(SocketException e) {
        System.err.println("Was not able to create a socket on port " + CLIENT_PORT);
        e.printStackTrace();
    } catch(UnknownHostException e) {
      System.err.println("Was not able to find the host");
      e.printStackTrace();
    }
  }
  
  public void run() {
    try {
      String temp = "Jimmy";
      buf = temp.getBytes();
      DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 2018);
      socket.send(packet); // currently sending an empty packet
      packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet); // currently receiving an empty packet
      String received = new String(packet.getData(), 0, packet.getLength());
    } catch(IOException e) {
      System.err.println("IOException thrown: problem receiving or sending packet");
      e.printStackTrace();
    }
    
    // do something with received String
    
  }
  
  public void close() {
    socket.close();
  }
  
}
