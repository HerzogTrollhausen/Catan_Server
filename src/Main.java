import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    static ArrayList<Client> clients = new ArrayList<>();
    public static ArrayList<Game> games = new ArrayList<>();
    static boolean accepting = true;
    static ServerSocket server;
    static int maxID;
    static Game zielSpiel;

    public static void main(String[] args)
    {
        try
        {
            final int PORT = 6677;//SET NEW CONSTANT VARIABLE: PORT
            server = new ServerSocket(PORT); //SET PORT NUMBER
            System.out.println("Waiting for clients...");//AT THE START PRINT THIS
            Accepter accepter = new Accepter();
            Thread acceptThread = new Thread(accepter);
            acceptThread.start();
            Scanner sc = new Scanner(System.in);
            while(true)
            {
                inputInterpreter(sc.nextLine());
            }
        } catch (Exception e)
        {
            System.out.println("An error occurred.");//IF AN ERROR OCCURRED THEN PRINT IT
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private static void inputInterpreter(String msg)
    {
        if(msg.charAt(0) == '/')
        {
            msg = msg.substring(1);
            String[] words = msg.split(" ");
            switch (words[0])
            {
                case "game":
                {

                }
            }
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
