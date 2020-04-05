import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;

public class Player {
    public String userName;
    public String characterName;
    public int health;
    public int characterIndex = -1;//chosen character player


    public InetAddress ip;
    public int port;

    public DataOutputStream out;
    public DataInputStream in;
    public String team = null;

    private GameRoom myRoom; //هم room می دونه کیا توش هستن هم player می دونه که با چه room ای کار داره

    public static int minPing = 50;//minimum ping for user -- >TODO will change by server if server was too busy

    //Vectors
    private float[] vector3_pos;
    private float rotation;


    //threads
    private Thread sendThread;
    private Thread reciveThread;

    private Thread chooseCharacterThread;
    public boolean characterChooseFinished = false;

    public GameRoom getMyRoom() {
        return myRoom;
    }

    public void setMyRoom(GameRoom myRoom) {
        this.myRoom = myRoom;
    }


    public Player(String userName, DataInputStream in, DataOutputStream out, InetAddress ip, int port) {
        vector3_pos = new float[3];
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.characterName = null;
        this.in = in;
        this.out = out;


        //todo refactor
        // String firstMassage = ClientThreads.reader(in); //amirkashi;p:1.254:2.65:3.65
        //الان فرض شده که نقطه ی اولیه رو کلاینت می گه فعلا برای تست هستش


        // String[] parts = firstMassage.split(";")[1].split(":");  // todo recive  //amirkashi;position:1.254:2.65:3.65


        // setPositionWithStr(parts, vector3_pos);


    }

    //we choose when to start thread for start transfering data (when room is full)
    public void startChooseCharacterSceneThread() {
        chooseCharacterThread = new Thread(new chooseCharacterScence());
        chooseCharacterThread.start();
        System.out.println("send and receive thread for choosing character scene is created for " + userName);
    }

    public void startInGameThreads() {


        //building in game threads
        sendThread = new Thread(new inGameSender());
        reciveThread = new Thread(new inGameSceneReceiver());
        sendThread.start();
        reciveThread.start();
        System.out.println("send and receive in game round thread is created for" + userName);
    }

    private class chooseCharacterScence implements Runnable {

        @Override
        public void run() {
            characterChooseFinished = false;

            ClientThreads.transmitter(out, "choose op!");//tell client to start choose character scene
            //this while will run until time for choosing ops finishes
            while (myRoom.currentState == 1) {
                String[] parsed1 = ClientThreads.reader(in).split("!");
                for (int j = 0; j < parsed1.length; j++) {
                    String[] parsed = parsed1[j].split(";");

                    if (parsed[0].equals("char")) { //player choosed character
                        int chooseCharacter = Integer.parseInt(parsed[1]);
                        boolean check = true;
                        for (int i = 0; i < myRoom.players.size(); i++) {
                            Player friend = myRoom.players.get(i);
                            if (!friend.userName.equals(userName) && friend.team.equals(team)) {
                                if (friend.characterIndex != -1 && friend.characterIndex == chooseCharacter) {
                                    check = false;
                                    break;
                                }
                            }
                        }
                        if (check) {//it is valid too choose character
                            characterIndex = chooseCharacter;
                            ClientThreads.transmitter(out, "accepted char!");
                            //now send every one else what he chose
                            for (int i = 0; i < myRoom.players.size(); i++) {
                                Player other = myRoom.players.get(i);
                                if (!other.userName.equals(userName)) {
                                    ClientThreads.transmitter(other.out, "char;" + userName + ";" + characterIndex + "!");
                                }
                            }
                        } else {
                            ClientThreads.transmitter(out, "wrong char!");
                        }
                    }
                    try {
                        Thread.sleep(minPing);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            characterChooseFinished = true;
        }
    }


    class inGameSceneReceiver implements Runnable {
        @Override
        public void run() {


            /* TCP
            while (myRoom.currentState == 3) {
                String[] parsed1 = ClientThreads.reader(in).split("!");
                for (int i = 0; i < parsed1.length; i++) {
                    String[] parsed = parsed1[i].split(";");
                    if (parsed[0].equals("trf")) {
                        String[] segment = parsed[1].split(":");
                        setPositionWithStr(segment, parsed[2]); //todo receive // place:142.254:54.26:22.11
                    }

                    try {
                        Thread.sleep(minPing);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            */
            //todo
        }
    }

    class inGameSender implements Runnable {
        @Override
        public void run() {

            //tell client to start game scene should be in tcp
            ClientThreads.transmitter(out, "start!");

            //todo send loaction of enemyies  disconnection is not handled now
            while (myRoom.currentState == 3) {//this while is when player goes into match and plays
                try {
                    Thread.sleep(minPing);
                    String send = "enpos;"; //todo send // enemyLocation:amirkashi:45.45:142.25:154.567;
                    for (int i = 0; i < myRoom.players.size(); i++) {
                        Player enemy = myRoom.players.get(i);
                        if (!enemy.userName.equals(userName)) {
                            send += enemy.userName + "," + enemy.vector3_pos[0] + ":" + enemy.vector3_pos[1] + ":" + enemy.vector3_pos[2] + "," + enemy.rotation + ",";
                        }
                    }
                    send = send.substring(0, send.length() - 1) + "!";
                   // ClientThreads.transmitter(out, send);
                    Udp.broadcastSystem(send,ip,port);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //tell client to end game scene
            ClientThreads.transmitter(out, "yield!");
            //todo
        }
    }


    public void setPositionWithStr(String floatText[], String rotationText) {//position:1:2:2
        vector3_pos[0] = Float.parseFloat(floatText[0]);
        vector3_pos[1] = Float.parseFloat(floatText[1]);
        vector3_pos[2] = Float.parseFloat(floatText[2]);
        rotation = Float.parseFloat(rotationText);

    }


}