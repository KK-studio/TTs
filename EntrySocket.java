import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EntrySocket {
    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    public void StartServer(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            while (true) {
                System.out.println("Waiting for a client ...");
                socket = server.accept();
                System.out.println("Client accepted");
                //   Thread objec = new Thread(new handler(socket,server));
                //  objec.start();


            }

            // close connection

        } catch (IOException i) {
            System.out.println("کلا نشد که بسازمش");
            System.out.println(i);
        }
    }
}

class ClientThreads implements Runnable { // this class use just for making threads in server
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientThreads(Socket socket) throws IOException {
        this.socket = socket;
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()) );
    }

    @Override
    public void run() {
        //in this section check user and pass and make User or find it in list


    }

    public static String reader(DataInputStream in) {
        try {

            int length = in.readInt();
            System.out.println(length);
            byte[] messageByte = new byte[length];
            boolean end = false;
            StringBuilder dataString = new StringBuilder(length);
            int totalBytesRead = 0;
            while (!end) {
                int currentBytesRead = in.read(messageByte);
                totalBytesRead = currentBytesRead + totalBytesRead;
                if (totalBytesRead <= length) {
                    dataString
                            .append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
                } else {
                    dataString
                            .append(new String(messageByte, 0, length - totalBytesRead + currentBytesRead,
                                    StandardCharsets.UTF_8));
                }
                if (dataString.length() >= length) {
                    end = true;
                }
            }
            System.out.print("");
            System.out.println(dataString);
            return dataString.toString();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("some problem in reading from socket");
        }
        return null;
    }

}