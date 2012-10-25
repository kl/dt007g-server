package chatserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public final class Logger extends Thread
{
  int i;

  public Logger(int i)
  {
    this.i = i;
    run();
  }

  public void run()
  {
    switch (this.i)
    {
    case 1:
      writefriends();

      break;
    }
  }

  public void writefriends()
  {
    try
    {
      FileWriter fstream = new FileWriter(Main.syspath + "/userlist.log");
      BufferedWriter out = new BufferedWriter(fstream);
      Iterator j = Main.userList.values().iterator();
      while (j.hasNext())
      {
        Friend f = (Friend)j.next();

        out.write("|" + f.getTime() + "|" + f.getNickname() + "|" + f.getFullname() + "|" + f.getAddress().toString() + "|" + f.getImage() + "|" + "\n");
      }

      out.close();
    }
    catch (Exception e)
    {
    }
  }
}