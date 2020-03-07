import java.util.ArrayList;
import java.util.Vector;

public class Player {
    public String userName;
    public String characterName;
    public int health ;

    private GameRoom myRoom; //هم room می دونه کیا توش هستن هم player می دونه که با چه room ای کار داره


    //Vectors
    private float[] vector3_pos ;
    private float[] vector3_rotation;

    public GameRoom getMyRoom() {
        return myRoom;
    }

    public void setMyRoom(GameRoom myRoom) {
        this.myRoom = myRoom;
    }


    public  Player(String userName){
        vector3_pos = new float[3];
        vector3_rotation = new float[3];
        this.userName = userName;
        this.characterName = null;

    }


    class reciverFromClient implements Runnable{

        @Override
        public void run() {
            //todo
        }
    }
    class sendToClient implements Runnable{
        @Override
        public void run() {
            //todo
        }
    }


}
