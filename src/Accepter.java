import java.io.IOException;
import java.net.Socket;

public class Accepter implements Runnable
{
    @Override
    public void run()
    {
        while (Main.accepting)//WHILE THE PROGRAM IS RUNNING
        {
            Socket s = null;//ACCEPT SOCKETS(CLIENTS) TRYING TO CONNECT
            try
            {
                s = Main.server.accept();

                System.out.println("Client connected from " + s.getLocalAddress().getHostName());    //	TELL THEM THAT THE CLIENT CONNECTED

                Client chat = new Client(s);//CREATE A NEW CLIENT OBJECT
                Main.clients.add(chat);
                Thread t = new Thread(chat);//MAKE A NEW THREAD
                t.start();//START THE THREAD
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
