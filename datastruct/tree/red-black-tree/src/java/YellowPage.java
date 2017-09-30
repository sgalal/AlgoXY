import java.util.*;
import java.io.*;

public class YellowPage {
    public static void main(String[] args) {
        Map<String, String> dict = new TreeMap<>();
        try {
            Scanner sc = new Scanner(new File("yp.txt"));
            while (sc.hasNext()) {
                String name = sc.next();
                String phone = sc.next();
                dict.put(name, phone);
            }
            sc = new Scanner(System.in);
            while (true) {
                System.out.print("name: ");
                String name = sc.next();
                String phone = dict.get(name);
                System.out.println(phone == null ? "not found" : ("phone: " + phone));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
}
