import java.util.*;

public class WordCount {
    public static void main(String[] args) {
        Map<String, Integer> dict = new TreeMap<>();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String key = sc.next();
            dict.put(key, 1 + (dict.containsKey(key) ? dict.get(key) : 0));
        }
        for (Map.Entry<String, Integer> e : dict.entrySet())
            System.out.format("%s: %d\n", e.getKey(), e.getValue());
    }
}
