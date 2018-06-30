import java.util.ArrayList;
import java.util.LinkedList;

public class Game
{
    private Client[] clients;
    private ArrayList<String> strings = new ArrayList<>();
    public int currentNumberOfPlayers;
    public String name;
    public int ID;

    Game(int size, String name, int ID)
    {
        clients = new Client[size];
        this.name = name;
        this.ID = ID;
    }

    public void anAlleSenden(String msg)
    {
        for (int i = 0; i < clients.length; i++)
        {
            if (clients[i] != null)
            {
                clients[i].sendToClient(msg);
            }
        }
        strings.add(msg);
    }

    void addClient(Client c)
    {
        for (int i = 0; i < clients.length; i++)
        {
            if (clients[i] == null)
            {
                c.setGame(this);
                clients[i] = c;
                c.setID(i);
                currentNumberOfPlayers++;
                sendOldMessagesToClient(c);
                return;
            }
        }
        throw new IllegalStateException("Kein freier Platz für den Client im Spiel gefunden.");
    }

    void sendOldMessagesToClient(Client c)
    {
        for (String string : strings)
        {
            c.sendToClient(string);
        }
    }

    void removeClient(Client c)
    {
        for (int i = 0; i < clients.length; i++)
        {
            if (clients[i] == c)
            {
                clients[i] = null;
                currentNumberOfPlayers--;
            }
        }
    }

    private int getFirstID()
    {
        for (int i = 0; true; i++)
        {
            if (clients[i] == null)
            {
                return i;
            }
        }
    }

    public int getMaxNumberOfPlayers()
    {
        return clients.length;
    }
}
