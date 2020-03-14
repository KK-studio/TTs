import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Vector;

public class Player {
    public String userName;
    public String characterName;
    public int health;
    public DataOutputStream out;
    public DataInputStream in;


    private GameRoom myRoom; //هم room می دونه کیا توش هستن هم player می دونه که با چه room ای کار داره


    //Vectors
    private float[] vector3_pos;
    private float[] vector3_rotation;


    //threads
    private Thread sendThread;
    private Thread reciveThread;

    public GameRoom getMyRoom() {
        return myRoom;
    }

    public void setMyRoom(GameRoom myRoom) {
        this.myRoom = myRoom;
    }


    public Player(String userName, DataInputStream in, DataOutputStream out) {
        vector3_pos = new float[3];
        vector3_rotation = new float[3];
        this.userName = userName;
        this.characterName = null;
        this.in = in;
        this.out = out;


        //todo refactor
        String firstMassage = ClientThreads.reader(in); //amirkashi;p:1.254:2.65:3.65
        //الان فرض شده که نقطه ی اولیه رو کلاینت می گه فعلا برای تست هستش
        String[] parts = firstMassage.split(";")[1].split(":");  // todo recive  //amirkashi;position:1.254:2.65:3.65
        setPositionWithStr(parts, vector3_pos);


        sendThread = new Thread(new sendToClient());
        reciveThread = new Thread(new reciverFromClient());
        sendThread.start();
        reciveThread.start();
        System.out.println("send and receive thread is created for" + userName);


    }


    class reciverFromClient implements Runnable {
        @Override
        public void run() {

            String[] parsed = ClientThreads.reader(in).split(";");
            for (String part : parsed) {
                String[] segment = part.split(":");
                if (segment[0].equals("place")) {
                    setPositionWithStr(segment, vector3_pos); //todo receive // place:142.254:54.26:22.11
                }
            }


            //todo
        }
    }

    class sendToClient implements Runnable {
        @Override
        public void run() {
            //todo send loaction of enemyies  disconnection is not handled now
            while (true) {
                try {
                    Thread.sleep(50);
                    String send = "enemyLocation:"; //todo send // enemyLocation:amirkashi:45.45:142.25:154.567;
                    for (int i = 0; i < myRoom.players.size(); i++) {
                        Player enemy = myRoom.players.get(i);
                        if (enemy.userName != userName) {
                            send += enemy.userName + ":" + enemy.vector3_pos[0] + ":" + enemy.vector3_pos[1] + ":" + enemy.vector3_pos[2] + ";";
                        }
                    }
                    ClientThreads.transmitter(out, send);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //todo
        }
    }

    public void setPositionWithStr(String floatText[], float[] positions) {//position:1:2:2
        vector3_pos[0] = Float.parseFloat(floatText[1]);
        vector3_pos[1] = Float.parseFloat(floatText[2]);
        vector3_pos[2] = Float.parseFloat(floatText[3]);
    }


}