import java.util.Map;





//structure

// we use Dictionary for finding User(by name)
public class User {
    public String name;
    private String pass;
    public int lastIndexGame;

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public static User checkInList(Map map , String name){
        return (User) map.get(name);
    }
}
