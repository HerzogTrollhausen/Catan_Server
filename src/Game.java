import java.util.ArrayList;
import java.util.List;

class Game
{
    private List<Client> clients;
    private int maxNumberOfPlayers;
    private ArrayList<String> strings = new ArrayList<>();
    int currentNumberOfPlayers;
    String name;
    int ID;

    Game(int size, String name, int ID)
    {
        clients = new ArrayList<>();
        maxNumberOfPlayers = size;
        this.name = name;
        this.ID = ID;
    }

    void anAlleSenden(String msg)
    {
        for (Client client : clients)
        {
            if (client != null)
            {
                client.sendToClient(msg);
            }
        }
        strings.add(msg);
    }

    void addClient(Client c)
    {
        for (int i = 0; i < maxNumberOfPlayers; i++)
        {
            if (i >= clients.size())
            {
                c.setGame(this);
                clients.add(c);
                c.setID(i);
                currentNumberOfPlayers++;
                sendOldMessagesToClient(c);
                return;
            }
            else if(clients.get(i) == null)
            {
                c.setGame(this);
                clients.set(i, c);
                c.setID(i);
                currentNumberOfPlayers++;
                sendOldMessagesToClient(c);
                return;
            }
        }
        throw new IllegalStateException("Kein freier Platz für den Client im Spiel gefunden. maxNumberOfPlayers: "+maxNumberOfPlayers);
    }

    private void sendOldMessagesToClient(Client c)
    {
        c.sendID();
        for (String string : strings)
        {
            c.sendToClient(string);
        }
    }

    void removeClient(Client c)
    {
        for (int i = 0; i < clients.size(); i++)
        {
            if (clients.get(i) == c)
            {
                clients.get(i).setGame(null);
                clients.set(i, null);
                currentNumberOfPlayers--;
            }
        }
    }

    @SuppressWarnings("unused")
    private int getFirstID()
    {
        for (int i = 0; true; i++)
        {
            if (clients.get(i) == null)
            {
                return i;
            }
        }
    }

    int getMaxNumberOfPlayers()
    {
        return maxNumberOfPlayers;
    }

    void setMaxNumberOfPlayers(int maxNumberOfPlayers)
    {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }
}
