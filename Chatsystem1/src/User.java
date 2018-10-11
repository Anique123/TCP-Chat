import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class User {

    private static final String host = "localhost";
    private static final int portNumber = 4645;
    //static final Scanner scan = new Scanner(System.in);

    //private List<String> userName;
    private String userName;
    private String serverHost;
    private int serverPort;

    public static void main(String[] args) throws Exception{
        Set<String> userName = new HashSet<>();
        String readName = "";
        String name = "";
        Scanner scan = new Scanner(System.in);
        System.out.println("Indtast JOIN for at forbinde til chatten");
        if(readName == null || readName.trim().equals("")){

            if(scan.hasNext("JOIN")){
                System.out.println("Velkommen");
                Thread.sleep(1000);
                Scanner scan1 = new Scanner(System.in);
                System.out.println("Indtast Brugernavn:");
                name = scan1.nextLine();

            }
            }


            /*if(readName.trim().equals("")){
                System.out.println("Ugyldigt Brugernavn. Skriv det ind igen:");
            }*/
        User client = new User(name, host, portNumber);
        client.startClient(scan);

    }

    private User(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void startClient(Scanner scan) throws Exception{
        try{
            Socket socket = new Socket(serverHost, serverPort);
            java.lang.Thread.sleep(500); // waiting for network communicating.

            ChatServerThread serverThread = new ChatServerThread(socket, userName);
            java.lang.Thread serverAccessThread = new java.lang.Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){


                if(scan.hasNextLine()){
                    if (scan.hasNext("quit")){
                        socket.close();
                        throw new InterruptedException();
                    }
                    serverThread.addMessageToSend(scan.nextLine());
                }
                // NOTE: scan.hasNextLine waits input (in the other words block this thread's process).
                // NOTE: If you use buffered reader or something else not waiting way,
                // NOTE: I recommends write waiting short time like following.
                // else {
                //    Thread.sleep(200);
                // }
            }
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", serverHost='" + serverHost + '\'' +
                ", serverPort=" + serverPort +
                '}';
    }
}
