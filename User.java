import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


//structure

// we use Dictionary for finding User(by name)
public class User {
    public static Map<String, User> users = new HashMap<>(); //data structure for users
    public String name;
    public int lastIndexGame; // show that last game that player was there
    public Player player = null; // اگر توی بازی نباشه باید null شود
    public DataOutputStream output;
    public DataInputStream input;

    private String pass;//it will remove when use database


    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        users.put(name, this);
    }

    public User(String name, String pass,DataInputStream input , DataOutputStream output) {
        this.name = name;
        this.pass = pass;
        this.input = input;
        this.output = output;
        users.put(name, this);
    }

    public Boolean checkPass(String pass){
        if(this.pass == pass){
            return true;
        }else {
            return false;
        }
    }

    public void socketConfigUser(DataInputStream input , DataOutputStream output){
        this.output = output;
        this.input = input;
    }

    public static User checkInList(Map map, String name) {
        return (User) map.get(name);
    }

    public synchronized void join() {
        player = new Player(name);
        //می شود برای پیدا کردن بازی خالی با اضافه کردن صف اضاف مدت زمان را پایین آورد
        for (Integer roomNum : GameRoom.games.keySet()) {
            //check that we have empty space in games or not
            if (GameRoom.games.get(roomNum).currentState == 0 && GameRoom.sizeRoom > GameRoom.games.get(roomNum).players.size()) {
                GameRoom.games.get(roomNum).players.add(player);//add to match
                this.lastIndexGame = GameRoom.games.get(roomNum).index;
                player.setMyRoom(GameRoom.games.get(roomNum));
                return;
            }

        }
        GameRoom newGame = new GameRoom();
        newGame.players.add(player); // add to match
        this.lastIndexGame = newGame.index;
        player.setMyRoom(newGame);
        return;

    }
}
