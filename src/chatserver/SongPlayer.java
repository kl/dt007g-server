package chatserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SongPlayer
  implements Runnable
{
  public void run()
  {
    LinkedList l = new LinkedList();
    try
    {
      File file = new File(Main.syspath + "/list");
      Scanner s = new Scanner(file);

      while (s.hasNext())
      {
        l.add(s.next());
      }
    }
    catch (FileNotFoundException ex) {
      Logger.getLogger(SongPlayer.class.getName()).log(Level.SEVERE, null, ex);
    }

    while (true)
    {
      for (int i = 0; i < l.size(); i++)
        try
        {
          Scanner lyrics = new Scanner(new File(Main.syspath + "/" + (String)l.get(i)));
          Main.currentsong = ((String)l.get(i)).replaceAll("_", " ");

          while (lyrics.hasNext())
            if (Main.socketList.isEmpty()) {
              try {
                Thread.currentThread(); Thread.sleep(5000L);
              } catch (InterruptedException ex) {
              }
            }
            else {
              Thread th = new Thread(new SongSender(lyrics.nextLine()));
              th.start();
              try {
                Thread.currentThread(); Thread.sleep(5000L);
              }
              catch (InterruptedException ex)
              {
              }
            }
        }
        catch (FileNotFoundException ex) {
          Logger.getLogger(SongPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }

  public static void main(String[] args)
  {
    SongPlayer player = new SongPlayer();
  }
}