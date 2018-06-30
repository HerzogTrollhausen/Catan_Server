import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main
{
    static ArrayList<Client> clients = new ArrayList<>();
    public static ArrayList<Game> games = new ArrayList<>();
    static int maxID;

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

                Client chat = new Client(s);//CREATE A NEW CLIENT OBJECT
                clients.add(chat);
                Thread t = new Thread(chat);//MAKE A NEW THREAD
                t.start();//START THE THREAD
            }
        } catch (Exception e)
        {
            System.out.println("An error occurred.");//IF AN ERROR OCCURRED THEN PRINT IT
            e.printStackTrace();
        }
    }

    public static String gamesToString()
    {
        StringBuilder sb = new StringBuilder("u");
        for (Game g : games)
        {
            sb.append(g.ID).append(g.currentNumberOfPlayers).append(g.getMaxNumberOfPlayers()).append(g.name).append('#');
        }
        return sb.toString();
    }

    public static void addGame(Game game)
    {
        games.add(game);
    }

    public static void addClientToGame(Client c, int ID)
    {
        Game g = games.get(ID);
        if(g.ID == ID)
        {
            g.addClient(c);
            return;
        }
        throw new IllegalArgumentException("IDs stimmen nicht überein: Argument-ID:"+ID+", Game-ID: "+g.ID);
    }

}
