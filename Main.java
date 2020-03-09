import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;


public class Main {

    public static void main(String[] args) {
//        Vector tt = new Vector(3);
//        Map<String,User> test = new Hashtable<String, User>();
//        test.put("hasan",new User("hasan","1234"));
//        test.put("hasan1",new User("hasan1","12345"));
//        test.put("hasan2",new User("hasan2","123456"));
//        for (String name:test.keySet() ) {
//            System.out.println(((User)test.get(name)).name);
//
//        }
        EntrySocket entrySocket = new EntrySocket();
        entrySocket.startServer(5000);

	// write your code here
    }
}
