import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client implements Runnable{

    private Socket socket;//SOCKET INSTANCE VARIABLE
    private PrintWriter out;
    private int ID;
    public boolean listening = true;
    private Game game;

    Client(Socket s)
    {
        socket = s;//INSTANTIATE THE SOCKET
        try
        {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        //sendToClient("k"+ID);
        sendToClient(Main.gamesToString());
    }

    @Override
    public void run() //(IMPLEMENTED FROM THE RUNNABLE INTERFACE)
    {
        try //HAVE TO HAVE THIS FOR THE in AND out VARIABLES
        {
            Scanner in = new Scanner(socket.getInputStream());
            //GET THE SOCKETS OUTPUT STREAM (THE STREAM YOU WILL SEND INFORMATION TO THEM FROM)

            while (listening)//WHILE THE PROGRAM IS RUNNING
            {
                if (in.hasNext())
                {
                    String input = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
                    System.out.println(ID+"->:" + input);//PRINT IT OUT TO THE SCREEN
                    getMessage(input);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();//MOST LIKELY THERE WONT BE AN ERROR BUT ITS GOOD TO CATCH
        }
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public void sendID()
    {
        sendToClient("k"+ID);
    }

    public void getMessage(String msg)
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
                 */
                case 'a':
                {
                    sendToClient(Main.gamesToString());
                    break;
                }
                case 'b':
                {
                    int maxAnzahlSpieler = Integer.parseInt(""+msg.charAt(1));
                    String name = msg.substring(2);
                    game = new Game(maxAnzahlSpieler, name, Main.maxID++);
                    game.addClient(this);
                    Main.addGame(game);
                    sendToClient("k"+ID);

                    break;
                }
                case 'c':
                {
                    sendToClient("k"+ID);
                    Main.addClientToGame(this, Integer.parseInt(""+msg.charAt(1)));
                    break;
                }
                case 'd':
                {
                    game.anAlleSenden("m"+game.currentNumberOfPlayers);
                    break;
                }
                case 'e':
                {
                    game.removeClient(this);
                    break;
                }
                case 'f':
                {
                    game = new Game(4, msg.substring(1), Main.maxID++);
                    game.addClient(this);
                    Main.addGame(game);
                }
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

    public void sendToClient(String msg)
    {
        System.out.println("->"+ID+":"+msg);
        out.println(msg);
        out.flush();
    }

}


