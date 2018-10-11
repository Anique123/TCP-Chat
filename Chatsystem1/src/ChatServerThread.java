import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ChatServerThread implements Runnable {
    private Socket socket;
    private String userName;
    private boolean isAlived;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    public ChatServerThread(Socket socket, String userName){
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
    }

    public void addMessageToSend(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    @Override
    // runnable metoden
    public void run(){
        System.out.println("Velkommen " + userName + "\n" + "Port: " + socket.getPort() + "\n" + "IPAdresse: " + socket.getRemoteSocketAddress());

        try{
            //BufferedOutputStream outputToServer = new BufferedOutputStream(socket.getOutputStream());
            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverInput = new Scanner(serverInStream);
            // BufferedReader userBr = new BufferedReader(new InputStreamReader(userInStream));
            // Scanner userIn = new Scanner(userInStream);

            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(serverInput.hasNextLine()){
                        System.out.println(serverInput.nextLine());
                    }
                }
                if(hasMessages){
                    String nextSend;
                    // synchronized for at undgå thread går over hinanden
                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    //Skriver til Server
                    //outputToServer.(userName + " > " + nextSend);
                    //serverOutput.println(userName + " > " + nextSend);
                    serverOutput.println(userName +": " + nextSend);
                    // tømmer Printwriter serverOutput for at der ik sendes noget fra sidste besked igen
                    serverOutput.flush();
                    //outputToServer.flush();
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
}