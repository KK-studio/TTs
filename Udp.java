import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Udp implements Runnable{
    public Udp(){ //listen all things :))    not thread available for you

        }

        // A utility method to convert the byte array
        // data into a string representation.
        public static StringBuilder data(byte[] a)
        {
            if (a == null)
                return null;
            StringBuilder ret = new StringBuilder();
            int i = 0;
            while (a[i] != 0)
            {
                ret.append((char) a[i]);
                i++;
            }
            return ret;
        }

    @Override
    public void run() {
        try {
        // Step 1 : Create a socket to listen at port 1234
        DatagramSocket ds = new DatagramSocket(1234);
        byte[] receive = new byte[2048];
        DatagramPacket DpReceive = null;
        while (true) {

            // Step 2 : create a DatgramPacket to receive the data.
            DpReceive = new DatagramPacket(receive, receive.length);

            // Step 3 : revieve the data in byte buffer.
            ds.receive(DpReceive);

            System.out.println("Client:-" + data(receive));

            // Exit the server if the client sends "bye"
            if (data(receive).toString().equals("bye")) {
                System.out.println("Client sent bye.....EXITING");
                break;
            }
            taskHandler(data(receive).toString(),DpReceive.getAddress(),DpReceive.getPort());
            // Clear the buffer after every message.
            receive = new byte[2048];
        }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void   taskHandler(String task , InetAddress ip,int port ){ //وظیفه ای که باید انجام شود در کلمه دوم باید بیان شود و کلمه اول نام هست **
        String[] taskSplited =  task.split(";");
        switch (taskSplited[1]){
            case "join":
                User.users.get(taskSplited[0]).joinToRoom(ip,port);
                break;
                // todo
        }
    }


    public  static void  broadcastSystem (String inp,InetAddress ip,int port){ // you can make defult for port with copy past  :)
        try {
            // Step 1:Create the socket object for
            // carrying the data.
            DatagramSocket ds = new DatagramSocket();

            //InetAddress ip = InetAddress.getLocalHost();   ///***/// see it if you forget format
            byte buf[] = null;

            // loop while user not enters "bye"
            while (true) {

                // convert the String input into the byte array.
                buf = inp.getBytes();

                // Step 2 : Create the datagramPacket for sending
                // the data.
                DatagramPacket DpSend =
                        new DatagramPacket(buf, buf.length, ip,port);

                // Step 3 : invoke the send call to actually send
                // the data.
                ds.send(DpSend);

                // break the loop if user enters "bye"
                if (inp.equals("bye"))
                    break;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

