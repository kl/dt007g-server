package chatserver;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientThread
        implements Runnable
{
    DataInputStream is = null;
    Socket s;
    DataInputStream dis;
    int i = 0;
    String id = "";

    public ClientThread(Socket clientSocket) {
        this.s = clientSocket;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                this.dis = new DataInputStream(this.s.getInputStream());

                String m = this.dis.readUTF();

                System.out.println(m);

                if (this.i == 0)
                {
                    String[] dm = m.split(">");

                    this.id = (this.id = dm[1].toUpperCase().replaceAll("<", "").replaceAll(">", ""));

                    Main.socketList.put(this.id, this.s);

                    System.out.println("ID: " + this.id);
                }

                if (m.startsWith("<LOGOUT>"))
                {
                    Thread th = new Thread(new ClientHandler(m));
                    th.start();
                    th.join();
                    this.s.close();
                }
                else
                {
                    Thread th = new Thread(new ClientHandler(m));

                    th.start();

                    this.i += 1;
                }
            }

        }
        catch (Exception ex)
        {
            if (Main.socketList.containsKey(this.id))
            {
                Main.socketList.remove(this.id);
                Main.userList.remove(this.id);
            }
        }
    }
}
