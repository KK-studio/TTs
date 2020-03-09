import java.util.*;


//structure

// we use Dictionary for finding GameRoom(by index) => user know last game index and must check it
public class GameRoom {
    public static int sizeRoom = 2; //muse define
    public static Map< Integer,GameRoom> games = new HashMap<>();  // data structure for games
    public static int maxIndex = 0;
//    public ArrayList< Thread> gameThreads ; //  برای محکم کاری در صورتی که خواستیم که بر روی ترد کنترل بیشتری داشته باشیم
    public int index;
    public ArrayList<Player> players;
    public int currentState = 0 ; //shows game started or not /// 0 => not completed players ; 1=> in menu ; 2 => in game  ;3 => finished

    public GameRoom() {
        maxIndex++;
        index = maxIndex;
        players = new ArrayList<Player>();
        currentState = 0;
        games.put(index , this);
//        gameThreads = new ArrayList<>();
    }

    public void addUserIngame(Player player ){

    }

    public void spawnFire() {
        //todo
    }

    public void endGame() {
        //todo
    }
}
