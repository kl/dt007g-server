package chatserver;

import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class SongSender
  implements Runnable
{
  String m;

  public SongSender(String _m)
  {
    this.m = ("<PUBLIC><LABADMIN><" + _m + ">");
  }

  public void run()
  {
    ProcessPublicMessages(this.m);
  }

  public void ProcessPublicMessages(String m)
  {
    System.out.println("SINGING TO THE PUBLIC: \n" + m);
    try
    {
      Iterator it = Main.socketList.values().iterator();

      while (it.hasNext())
      {
        Socket f = (Socket)it.next();

        DataOutputStream dos = new DataOutputStream(f.getOutputStream());
        dos.writeUTF(m);
      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}