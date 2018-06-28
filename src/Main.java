import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main
{

    static LinkedList<String> q = new LinkedList<String>();
    static ArrayList<Client> clients = new ArrayList<Client>();
    static int ID = 0;

    public static void anAlleSenden(String msg)
    {
        for(int i = 0; i < clients.size(); i++)
        {
            clients.get(i).sendToClient(msg);
            //System.out.println(msg+" an Client "+i+" gesendet.");
        }
    }

    public static void main(String[] args) throws IOException
    {
        try
        {
            final int PORT = 6677;//SET NEW CONSTANT VARIABLE: PORT
            ServerSocket server = new ServerSocket(PORT); //SET PORT NUMBER
            System.out.println("Waiting for clients...");//AT THE START PRINT THIS

            while (true)//WHILE THE PROGRAM IS RUNNING
            {
                Socket s = server.accept();//ACCEPT SOCKETS(CLIENTS) TRYING TO CONNECT

                System.out.println("Client connected from " + s.getLocalAddress().getHostName());    //	TELL THEM THAT THE CLIENT CONNECTED

                Client chat = new Client(s, ID++);//CREATE A NEW CLIENT OBJECT
                clients.add(chat);
                Thread t = new Thread(chat);//MAKE A NEW THREAD
                t.start();//START THE THREAD
            }
        } catch (Exception e)
        {
            System.out.println("An error occured.");//IF AN ERROR OCCURED THEN PRINT IT
            e.printStackTrace();
        }
    }

}
