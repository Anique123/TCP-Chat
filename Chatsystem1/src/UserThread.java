import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UserThread implements Runnable {
    private Socket socket;
    private PrintWriter userOut;
    private ChatServer server;

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
            Scanner in = new Scanner(socket.getInputStream());

            // starter kommunikationen
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    // NOTE: if you want to check server can read input, uncomment next line and check server file console.
                    // System.out.println(input);

                    for(UserThread thatClient : server.getUsers()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(input + "\r\n");
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
