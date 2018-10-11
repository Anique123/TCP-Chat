import java.awt.*;
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

    private User(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    public static void main(String[] args) throws Exception{
        Set<String> userName = new HashSet<>();
        String readName = "";
        String name = "";
        Scanner scan = new Scanner(System.in);
        System.out.println("Vigtige protokoler i chatten: \n JOIN: deltag i chatten \n IMAV: Giv besked på du stadig er aktiv");
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
                    if(scan.hasNext("IMAV")){
                        System.out.println(userName + " is still here");
                    }
                    if (scan.hasNext("quit") || scan.hasNext("QUIT") || scan.hasNext("Quit")){
                        socket.close();
                        throw new InterruptedException();
                    }
                    serverThread.addMessageToSend(scan.nextLine());
                }

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
