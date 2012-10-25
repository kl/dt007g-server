package chatserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ChatToAlice
{
  static Socket socket;
  private static PrintWriter out;
  private static BufferedReader in;

  public void GetAliceResponse(Socket clientsocket, String m)
  {
    String[] dm = m.split(">", 4);
    String message = dm[3].replaceAll("<", "").replaceAll(">", "");
    try
    {
      InetAddress host = InetAddress.getByName("193.10.119.33");

      socket = new Socket(host.getHostName(), 23456);
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out.println(message);
      System.out.println("Sent to Alice:   " + message);
      String response = in.readLine();
      DataOutputStream dos = new DataOutputStream(clientsocket.getOutputStream());
      dos.writeUTF("<PRIVATE><ALICE><" + response + ">");
      System.out.println("Replied Alice, :   " + response);
      socket.close();
      socket = null;
      out = null;
      in = null;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}