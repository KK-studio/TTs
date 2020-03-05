import java.util.ArrayList;


//structure

// we use Dictionary for finding GameRoom(by index) => user know last game index and must check it
public class GameRoom {
    public static int maxIndex = 0;
    public int index;
    public ArrayList<Player> players;

    public GameRoom() {
        maxIndex++;
        index = maxIndex;
        players = new ArrayList<Player>();
    }

    public void spawnFire() {
        //todo
    }

    public void endGame() {
        //todo
    }
}
