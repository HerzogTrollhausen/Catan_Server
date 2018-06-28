import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client implements Runnable{

    private Socket socket;//SOCKET INSTANCE VARIABLE
    private PrintWriter out;
    private int ID;
    private boolean listening = true;

    Client(Socket s, int ID)
    {
        this.ID = ID;
        socket = s;//INSTANTIATE THE SOCKET
        try
        {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        sendToClient("k"+ID);
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
                    Main.anAlleSenden(input);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();//MOST LIKELY THERE WONT BE AN ERROR BUT ITS GOOD TO CATCH
        }
    }

    public void sendToClient(String msg)
    {
        System.out.println("->"+ID+":"+msg);
        out.println(msg);
        out.flush();
    }

}


