import java.util.Map;

public class User {
    public String name;
    private String pass;

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public static User checkInList(Map map , String name){
        return (User) map.get(name);
    }
}
