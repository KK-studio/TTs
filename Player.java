import java.util.ArrayList;
import java.util.Vector;

public class Player {
    public String userName;
    public String characterName;
    public int health ;

    //Vectors
    private float[] vector3_pos ;
    private float[] vector3_rotation;

    public  Player(){
        vector3_pos = new float[3];
        vector3_rotation = new float[3];
    }

    class reciverFromClient implements Runnable{

        @Override
        public void run() {

        }
    }
    class sendToClient implements Runnable{
        @Override
        public void run() {

        }
    }


}
