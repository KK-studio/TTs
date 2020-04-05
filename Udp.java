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
        byte[] receive = new byte[2048];
        DatagramPacket DpReceive = null;
        DatagramSocket ds = new DatagramSocket(7979);
        while (true) {


            receive = new byte[2048];
            // Step 2 : create a DatgramPacket to receive the data.
            DpReceive = new DatagramPacket(receive, receive.length);

            // Step 3 : revieve the data in byte buffer.
            ds.receive(DpReceive);

            System.out.println("Client:-" + data(receive));
            taskHandler(data(receive).toString(),DpReceive);
//            broadcastSystem("wtfff man "+data(receive),DpReceive.getAddress(),5030);
            // Exit the server if the client sends "bye"
            if (data(receive).toString().equals("bye")) {
                System.out.println("Client sent bye.....EXITING");
                break;
            }
//            ds.close();
            // Clear the buffer after every message.
            try {
                Thread.sleep(9);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void   taskHandler(String task,DatagramPacket datagramPacket){ //وظیفه ای که باید انجام شود در کلمه دوم باید بیان شود و کلمه اول نام هست **
        String[] taskSplited =  task.split("!");
        for (int i=0;i<taskSplited.length;i++) {
            String [] data = taskSplited[i].split(";");
            try {
                Player player = User.users.get(data[0]).player;
                if (player != null) {
                    if(!player.ip.equals(datagramPacket.getAddress())){
                        player.ip = datagramPacket.getAddress();
                       // player.port = datagramPacket.getPort();
                    }
                    switch (data[1]) {
                        case "join":
                            // User.users.get(taskSplited[0]).joinToRoom(ip,port);
                            break;
                        case "trf":
                            String[] segment = data[2].split(":");
                            player.setPositionWithStr(segment, data[3]);
                            break;
                        // todo
                    }
                }
            }
            catch (NullPointerException e){
                System.out.println("null pish miad");
            }
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


                // convert the String input into the byte array.
                buf = inp.getBytes();

                // Step 2 : Create the datagramPacket for sending
                // the data.
                DatagramPacket DpSend =
                        new DatagramPacket(buf, buf.length, ip,port);

                // Step 3 : invoke the send call to actually send
                // the data.
                ds.send(DpSend);
                //System.out.println("to ip " + ip +" data sent :"+inp);
                // break the loop if user enters "bye"
                ds.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            System.out.println("null ip maybe");
        }
    }
}

