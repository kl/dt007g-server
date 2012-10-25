package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler
  implements Runnable
{
  DatagramPacket dp;
  String now;
  Socket s;
  String m;
  DataInputStream dis;
  String id;
  String[] dm;

  public ClientHandler(String m)
  {
    try
    {
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = new Date();
      this.now = dateFormat.format(date);
      this.m = m;
      this.dm = m.split(">");
      this.id = this.dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", "");
      this.s = ((Socket)Main.socketList.get(this.id));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void run()
  {
    if (this.m.startsWith("<REGISTER>"))
    {
      ProcessRegistration(this.m);
    }
    else if (this.m.startsWith("<PUBLIC>"))
    {
      ProcessPublicMessages(this.m);
    }
    else if (this.m.startsWith("<PRIVATE>"))
    {
      ProcessPrivateMessages(this.m);
    }
    else if (this.m.startsWith("<LOGOUT>"))
    {
      ProcessLogout(this.m);
    }
  }

  public void ProcessRegistration(String m)
  {
    if (Main.userList.containsKey(this.dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", "").toString()))
    {
      try
      {
        DataOutputStream dos = new DataOutputStream(this.s.getOutputStream());
        dos.writeUTF("<REGISTRATION_FAILED><USERNAME_EXISTS><MAYBE YOU HAVE NOT LOGGED OUT CORRECTLY, THE PROTOCOL SAYS YOU MUST DO THAT, IF YOU ARE TESTING, CHANGE YOUR NICKNAME AND TRY AGAIN!");

        ((Socket)Main.socketList.get(this.dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""))).close();
        Main.socketList.remove(this.dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""));
      }
      catch (IOException ex)
      {
      }

      return;
    }

    Main.userList.put(this.dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""), new Friend(this.dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""), this.dm[2].replaceAll("<", "").replaceAll(">", ""), this.s.getInetAddress(), this.dm[4].replaceAll("<", "").replaceAll(">", ""), this.now));

    System.out.println("NEW USER HAS BEEN ADDED TO THE DATABASE WITH NICKNAME:  " + this.id);
    try
    {
      System.out.println("TRANSMITTING THE FRIENDS LIST TO NEW USER:: " + this.id);

      Iterator i = Main.userList.values().iterator();

      while (i.hasNext())
      {
        String sendmessage = "";

        Friend f = (Friend)i.next();

        sendmessage = "<FRIEND><" + f.getNickname() + ">" + "<" + f.getFullname() + ">" + "<" + f.getAddress().getHostAddress() + ">" + "<" + f.getImage() + ">";

        DataOutputStream dos = new DataOutputStream(this.s.getOutputStream());
        dos.writeUTF(sendmessage);
      }

    }
    catch (Exception ex)
    {
      System.out.println("FAILED TO SEND THE FRIENDS LIST TO: " + this.id);
    }

    try
    {
      System.out.println("SENDING NEW USER ANNOUNCEMENT TO THE ENTIRE CHATROOM");

      String sendmessage = "<FRIEND>" + this.dm[1].toUpperCase() + ">" + this.dm[2] + ">" + "<" + this.s.getInetAddress().getHostAddress() + ">" + this.dm[4] + ">";

      Iterator it = Main.socketList.values().iterator();

      while (it.hasNext())
      {
        Socket f = (Socket)it.next();

        if (!f.equals(this.s)) {
          DataOutputStream dos = new DataOutputStream(f.getOutputStream());
          dos.writeUTF(sendmessage);
        }

      }

      System.out.println("NEW USER " + this.id + " HAS BEEN ANNOUNCE TO THE ENTIRE CHATROOM");
    }
    catch (Exception ex)
    {
      System.out.println("NEW USER " + this.id + " HAS NOT BEEN ANNOUNCE TO THE ENTIRE CHATROOM");
    }

    new Logger(1);

    String welcomemessage = "<PUBLIC><LABADMIN><Hey " + this.id.toUpperCase() + " ! ! !\nWelcome to the Java DT007G Chatrooms. " + "This message confirms that you can communicate with the server. \nI am currently singing: " + Main.currentsong + "\nTry and talk to ALICE she is a chatbot and will respond.";

    ProcessPublicMessages(welcomemessage);
  }

  public void ProcessPublicMessages(String m)
  {
    System.out.println("BROADCASTING NEW PUBLIC MESSAGE: \n" + m);
    try
    {
      Iterator it = Main.socketList.values().iterator();

      while (it.hasNext())
      {
        Socket f = (Socket)it.next();

        DataOutputStream dos = new DataOutputStream(f.getOutputStream());
        dos.writeUTF(m);
      }

      System.out.println("PUBLIC MESSAGE WAS BROADCASTED TO ALL MEMBERS");
    }
    catch (Exception ex)
    {
      System.out.println("PUBLIC MESSAGE WAS NOT BROADCASTED TO ALL MEMBERS");

      ex.printStackTrace();
    }
  }

  public void ProcessPrivateMessages(String m)
  {
    String[] dm = m.split(">", 4);
    try
    {
      if (dm[2].toUpperCase().replaceAll("<", "").replaceAll(">", "").equalsIgnoreCase("ALICE"))
      {
        Socket pvtsocket = (Socket)Main.socketList.get(dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""));
        ChatToAlice cta = new ChatToAlice();
        cta.GetAliceResponse(pvtsocket, m);
      }
      else
      {
        String pvtmessage = "<PRIVATE>" + dm[1] + ">" + dm[3];

        Socket pvtsocket = (Socket)Main.socketList.get(dm[2].toUpperCase().replaceAll("<", "").replaceAll(">", ""));

        DataOutputStream dos = new DataOutputStream(pvtsocket.getOutputStream());

        dos.writeUTF(pvtmessage);
      }

      System.out.println("A NEW PRIVATE MESSAGE HAS BEEN RELAYED");
    }
    catch (Exception ex)
    {
      System.out.println("A NEW PRIVATE MESSAGE HAS NOT BEEN RELAYED");
    }
  }

  public void ProcessLogout(String m)
  {
    System.out.println("PROCESSING LOGOUT REQUEST FOR: " + this.id);

    String[] dm = m.split(">");
    try
    {
      String sendmessage = "<LOGOUT><" + this.id + ">";

      Iterator it = Main.socketList.values().iterator();

      while (it.hasNext())
      {
        Socket f = (Socket)it.next();

        DataOutputStream dos = new DataOutputStream(f.getOutputStream());
        dos.writeUTF(m);
      }

      Main.userList.remove(dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""));
      ((Socket)Main.socketList.get(dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""))).close();
      Main.socketList.remove(dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""));

      System.out.println("LOGOUT NOTIFICATION WAS BROADCASTED TO ALL USERS");
    }
    catch (Exception ex)
    {
      System.out.println("LOGOUT NOTIFICATION WAS NOT BROADCASTED TO ALL USERS");
    }
  }
}