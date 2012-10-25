package chatserver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
  public static ConcurrentHashMap<String, Friend> userList;
  public static ConcurrentHashMap<String, Socket> socketList;
  public static File userlogfile;
  public static String syspath;
  public static String currentsong;

  public static void main(String[] args)
  {
    System.out.println("Chat Server is Running");
    syspath = System.getProperty("user.dir");
    System.out.println("Logs are being written to:  " + syspath);
    userList = new ConcurrentHashMap();
    socketList = new ConcurrentHashMap();
    currentsong = "";
    try
    {
      ServerSocket ss = new ServerSocket(8000);
      userList.put("ALICE", new Friend("ALICE", "Alice the Robot", InetAddress.getLocalHost(), "NONE; I AM JUST A ROBOT", "2010/10/08 10:36:11"));

      Thread singingthread = new Thread(new SongPlayer());
      singingthread.start();
      while (true)
      {
        Socket s = ss.accept();

        System.out.println(s.getInetAddress().getHostAddress());

        Thread th = new Thread(new ClientThread(s));

        th.start();
      }

    }
    catch (IOException ex)
    {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}