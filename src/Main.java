import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    static ArrayList<Client> clients = new ArrayList<>();
    private static ArrayList<Game> games = new ArrayList<>();
    static boolean accepting = true;
    static ServerSocket server;
    private static int maxID;
    private static boolean running = true;

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
            while(running)
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
                    break;
                }
                case "q":
                {
                    System.out.println("Beenden");
                    System.exit(-2);
                }
            }
        }
        else
        {

        }
    }

    static String gamesToString()
    {
        StringBuilder sb = new StringBuilder("u");
        for (Game g : games)
        {
            sb.append(g.ID).append(g.currentNumberOfPlayers).append(g.getMaxNumberOfPlayers()).append(g.name).append('#');
        }
        return sb.toString();
    }

    static void addGame(Game game)
    {
        games.add(game);
    }

    static void addClientToGame(Client c, int ID)
    {
        Game g = games.get(ID);
        if(g.ID == ID)
        {
            g.addClient(c);
            return;
        }
        throw new IllegalArgumentException("IDs stimmen nicht überein: Argument-ID:"+ID+", Game-ID: "+g.ID);


    }

    static void getMessage(String msg, Client client, Game game)
    {
        if(msg.charAt(0) == '?')
        {
            msg = msg.substring(1);
            switch(msg.charAt(0))
            {
                /*
                a: Gib die Games zurück
                bTest4: Erstellt ein Game namens Test mit bis zu 4 Spielern
                c3: Tritt spiel mit ID 3 bei
                d: Starte das Spiel
                e: Verlasse das Spiel
                fTest: Erstelle ein Spiel namens Test, das aus einer Datei geladen wird

                q: Beende den Server
                 */
                case 'a':
                {
                    client.sendToClient(Main.gamesToString());
                    break;
                }
                case 'b':
                {
                    int maxAnzahlSpieler = Integer.parseInt(""+msg.charAt(1));
                    String name = msg.substring(2);
                    game = new Game(maxAnzahlSpieler, name, Main.maxID++);
                    game.addClient(client);
                    Main.addGame(game);
                    client.sendToClient("k"+client.ID);

                    break;
                }
                case 'c':
                {
                    client.sendToClient("k"+client.ID);
                    Main.addClientToGame(client, Integer.parseInt(""+msg.charAt(1)));
                    break;
                }
                case 'd':
                {
                    game.anAlleSenden("m"+game.currentNumberOfPlayers);
                    break;
                }
                case 'e':
                {
                    if(game != null) {
                        game.removeClient(client);
                    }
                    clients.remove(client);
                    client.listening = false;
                    break;
                }
                case 'f':
                {
                    game = new Game(4, msg.substring(1), Main.maxID++);
                    game.addClient(client);
                    Main.addGame(game);
                }Main.running = false;

                default:
                {
                    game.anAlleSenden("qFehler#Unbekannte Anfrage an Server: "+msg);
                    throw new IllegalArgumentException("Unbekannte Anfrage an Server: "+msg);
                }
            }
        }
        else
        {
            if(msg.charAt(0) == 'm')
            {
                int i = 0;
                while(game == null)
                {
                    System.out.println(i);
                }
                game.setMaxNumberOfPlayers(Integer.parseInt(""+msg.charAt(1)));
            }
            game.anAlleSenden(msg);
        }
    }
}
