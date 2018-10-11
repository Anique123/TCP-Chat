import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

    public class ChatServer {

        // Instansiere Liste til at gemme brugere
        private List<UserThread> users;


        // Portnummer til at connect
        private static final int portNumber = 4645;
        private int serverPort;

        public static void main(String[] args){
            // Laver et objektet server af ChatServer
            ChatServer server = new ChatServer(portNumber);
            // Kalder metoden startServer() på det oprettede objekt
            server.startServer();
        }

        // Giver field "serverPort" portnummer 4444
        public ChatServer(int portNumber){
            this.serverPort = portNumber;
        }

        public List<UserThread> getUsers(){
            return users;
        }

        private void startServer(){
            // Laver listen users til en arraylist der gemmer UserThread objekter
            users = new ArrayList<UserThread>();

            // ServerSocket serverSocket = null;
            try {
                ServerSocket serverSocket = new ServerSocket(serverPort);
                acceptUsers(serverSocket);
            } catch (IOException e){
                System.err.println("Følgende port er ikke mulig: "+serverPort);
                System.exit(1);
            }
        }

        // Den oprettede ServerSocket som parametre så Users accepteres til ServerSocket'en
        private void acceptUsers(ServerSocket serverSocket){

            System.out.println("Serveren starter port: " + serverSocket.getLocalSocketAddress());
            while(true){
                try{
                    // Laver Socket så klienten/user kan connect til ServerSocket
                    Socket socket = serverSocket.accept();
                    System.out.println("Accepterer: : " + socket.getRemoteSocketAddress());
                    // Laver et User objekt hvor ChatServer tages som parameter og den instansieret socket
                    UserThread user = new UserThread(this, socket);
                    // laver en thread til den nye user så der kan køres flere users på samme tid på ServerSocket
                    Thread thread = new Thread(user);
                    thread.start();
                    //adder user til arraylist users
                    users.add(user);
                    /*for(int i = 0; i < clients.size(); i++){
                        System.out.println(clients.get(i));

                    }*/
                } catch (IOException ex){
                    System.out.println("Fejl på accept i: "+serverPort);
                }
            }
        }
    }
