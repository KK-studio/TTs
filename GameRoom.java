import java.io.DataOutputStream;
import java.util.*;


//structure

// we use Dictionary for finding GameRoom(by index) => user know last game index and must check it
public class GameRoom {
    public static int sizeRoom = 2; //muse define
    public static Map< Integer,GameRoom> games = new HashMap<>();  // data structure for games
    public static int maxIndex = 0;
//    public ArrayList< Thread> gameThreads ; //  برای محکم کاری در صورتی که خواستیم که بر روی ترد کنترل بیشتری داشته باشیم (فعلا این بخش کامته)ع
    public int index;
    public ArrayList<Player> players;
    public ArrayList<Player> blueTeamPlayers;
    public ArrayList<Player> redTeamPlayers;
    public int currentState = 0 ; //shows game started or not /// 0 => not completed players ; 1=> in menu ;2=>building map scene;3 => in game  ;4 => finished
    public int round = 1;//which round is player in
    public int mapIndex = 0; // what map index is game room playing

    public static final String teamRed = "red";
    public static final String teamBlue = "blue";

    public static final int maxMapIndex = 1; //how many maps we have

    public static final long choosingCharacterTime = 1000;//mili seconds should wait for players to choose player character
    public static final long roundTime = 1500000;//2 min and half

    public static final long maxRoundNumber = 3;

    private Thread gameRoomManager;
    public GameRoom() {
        maxIndex++;
        index = maxIndex;
        players = new ArrayList<Player>();
        redTeamPlayers = new ArrayList<Player>();
        blueTeamPlayers = new ArrayList<Player>();
        currentState = 0;
        games.put(index , this);
//        gameThreads = new ArrayList<>();
    }

    public void addUserIngame(Player player ){
        this.players.add(player);
        if(sizeRoom == players.size()){//check if room is full
            enablePlayersThread();
        }
    }

    public void enablePlayersThread(){
        System.out.println("setting initial values");
        //enable all player's threads and set initial values and making team members

        Random rand = new Random();//TODO later we may have squads
        String team1 = teamBlue;
        String team2 = teamRed;
        if(rand.nextInt()%2 == 0){
             team1 = teamRed;
             team2 = teamBlue;
        }

        mapIndex = rand.nextInt()%maxMapIndex;

        for (int i=0;i<sizeRoom/2;i++){
            players.get(i).team = team1;
        }
        for (int i=sizeRoom/2;i<sizeRoom;i++){
            players.get(i).team = team2;
        }

        for (int i=0;i<players.size();i++){
            if(players.get(i).team.equals(teamRed)){
                redTeamPlayers.add(players.get(i));
            }
            else {
                blueTeamPlayers.add(players.get(i));
            }
        }
        startRoom();
    }

    public void startRoom(){
        System.out.println("starting Game loop thread for game room :"+index);
        gameRoomManager = new Thread(new GameRoomManager());
        gameRoomManager.start();
    }
    public void spawnFire() {
        //todo
    }

    public void endGame() {
        //todo
    }

    class GameRoomManager implements Runnable{

        @Override
        public void run() {
            try {


                //telling clients initial values "friends enemies and map index"
                for (int i=0;i<players.size();i++){
                    Player player = players.get(i);
                    DataOutputStream outputStream = player.out;

                    //finding friends and enemies
                    String friends="";
                    String enemies="";
                    for (int j=0;j<players.size();j++){
                        Player other = players.get(j);
                        if(i != j) {
                            if (other.team.equals(player.team)) {
                                friends += other.userName;
                                friends += ",";
                            }
                            else  {
                                enemies += other.userName;
                                enemies += ",";
                            }
                        }

                    }
                    if(friends.length() != 0)
                    friends = friends.substring(0,friends.length()-1);
                    enemies = enemies.substring(0,enemies.length()-1);

                    String send = "room;"+mapIndex+";"+player.team+";"+ friends+";"+enemies+"!";
                    ClientThreads.transmitter(outputStream,send);
                }


                //main game loop
                while (round <= maxRoundNumber) {
                    ////-------------------- state = 1-----------------------------////
                    currentState = 1; //let players choose ops
                    for (int i=0;i<players.size();i++){
                        players.get(i).startChooseCharacterSceneThread();//start thread for sending and receiving data in character choose scene
                    }
                    Thread.sleep(choosingCharacterTime);//wait for players to choose

                    ////-----------------------------------------------------------////


                    ////-------------------- state = 2-----------------------------////
                    currentState = 2;//TODO sending important data before starting game scene
                    //TODO some data here...(ex choose random character for player who didnt choose)

                    for (int i=0;i<players.size();i++){
                        Player player = players.get(i);
                        while (!player.characterChooseFinished){//wait to send and recieve last data
                            Thread.sleep(50);
                        }
                    }

                    ////-----------------------------------------------------------////

                    ////-------------------- state = 3-----------------------------////
                    //start game round
                    currentState = 3;
                    for (int i=0;i<players.size();i++){
                        players.get(i).startInGameThreads();//start thread for sending and receiving data in game round
                    }
                    //TODO code here
                    Thread.sleep(roundTime);//TODO change this to loop for better handling
                    ////-----------------------------------------------------------////
                    round++;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ////-------------------- state = 4-----------------------------////
            currentState = 4;//ending game and sending some final data to client

            for (int i=0;i<players.size();i++) {
                Player player = players.get(i);
                DataOutputStream outputStream = player.out;
                ClientThreads.transmitter(outputStream,"end!");
                //TODO ...
            }
            ////-----------------------------------------------------------////
        }
    }

}
