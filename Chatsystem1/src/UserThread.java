import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UserThread implements Runnable {
    private Socket socket;
    private PrintWriter userOut;
    private ChatServer server;

    //Kører kun hvis der en server fra chatserver klassen
    public UserThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return userOut;
    }

    @Override
    public void run() {
        try{
            // sætter op kommunikation måder
            this.userOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner input = new Scanner(socket.getInputStream());

            // starter kommunikationen
            while(!socket.isClosed()){
                if(input.hasNextLine()){
                    String in = input.nextLine();


                    for(UserThread thatClient : server.getUsers()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(in + "\r\n");
                            thatClientOut.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
