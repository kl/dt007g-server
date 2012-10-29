package chatserver;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{

    public static ConcurrentHashMap<String, Friend> userList;
    public static ConcurrentHashMap<String, Socket> socketList;
    public static File userlogfile;
    public static String syspath = System.getProperty("user.dir");
    public static String currentsong;

    public static final String PROPERTIES_FILE_PATH = syspath + "/server.properties";
    public static Properties properties;

    public static void main(String[] args)
    {
        readProperties();

        System.out.println("Chat Server is Running");

        System.out.println("Logs are being written to: " + syspath);
        userList = new ConcurrentHashMap<String, Friend>();
        socketList = new ConcurrentHashMap<String, Socket>();
        currentsong = "";

        try {
            int port = Integer.parseInt(properties.getProperty("server_port"));
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Listening on port: " + port);

            userList.put("ALICE", new Friend("ALICE", "Alice the Robot", InetAddress.getLocalHost(), "NONE; I AM JUST A ROBOT", "2010/10/08 10:36:11"));

            Thread singingthread = new Thread(new SongPlayer());
            singingthread.start();

            while (true) {
                Socket s = ss.accept();

                System.out.println(s.getInetAddress().getHostAddress());

                Thread th = new Thread(new ClientThread(s));

                th.start();
            }
        }
        catch (IOException ex) {
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void readProperties() {
        try {
            FileInputStream stream = new FileInputStream(PROPERTIES_FILE_PATH);

            properties = new Properties();
            properties.load(stream);

        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}