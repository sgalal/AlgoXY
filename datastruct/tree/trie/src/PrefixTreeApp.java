import java.util.*;
import java.util.stream.*;
import java.lang.Math;
import java.lang.Exception;

public class PrefixTreeApp {
    // returns up to n candidates start with given prefix
    public static <T> List<Map.Entry<String, T>> lookup(PrefixTree.Node<T> t,
                                                        String key, int n) {
        if (t == null)
            return Collections.emptyList();
        String prefix = "";
        boolean match;
        do {
            match = false;
            for (Map.Entry<String, PrefixTree.Node<T>> entry : t.subTrees.entrySet()) {
                String k = entry.getKey();
                PrefixTree.Node<T> tr = entry.getValue();
                if (k.startsWith(key)) { // key is prefix of k
                    return expand(prefix + k, tr, n);
                }
                if (key.startsWith(k)) {
                    match = true;
                    key = key.substring(k.length());
                    t = tr;
                    prefix = prefix + k;
                    break;
                }
            }
        } while (match);
        return Collections.emptyList();
    }

    static <T> List<Map.Entry<String, T>> expand(String prefix, PrefixTree.Node<T> t, int n) {
        List<Map.Entry<String, T>> res = new ArrayList<>();
        Queue<Map.Entry<String, PrefixTree.Node<T> >> q = new LinkedList<>();
        q.offer(entryOf(prefix, t));
        while(res.size() < n && !q.isEmpty()) {
            Map.Entry<String, PrefixTree.Node<T>> entry = q.poll();
            String s = entry.getKey();
            PrefixTree.Node<T> tr = entry.getValue();
            if (tr.value.isPresent()) {
                res.add(entryOf(s, tr.value.get()));
            }
            for (Map.Entry<String, PrefixTree.Node<T>> e :
                     new TreeMap<>(tr.subTrees).entrySet()) {
                q.offer(entryOf(s + e.getKey(), e.getValue()));
            }
        }
        return res;
    }

    static <K, V> Map.Entry<K, V> entryOf(K key, V val) {
        return new AbstractMap.SimpleImmutableEntry<K, V>(key, val);
    }

    // T9 map

    static final Map<Character, String> mapT9 = new HashMap<Character, String>(){{
            put('1', ",."); put('2', "abc"); put('3', "def");
            put('4', "ghi"); put('5', "jkl"); put('6', "mno");
            put('7', "pqrs"); put('8', "tuv"); put('9', "wxyz");
        }};

    static final Map<Character, Character> rmapT9 = mapT9.entrySet().stream()
        .flatMap(e -> e.getValue().chars().mapToObj(i -> (char)i)
                 .map(c -> entryOf(c, e.getKey())))
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

    public static class Test {
        final static String[] testKeys =
            new String[]{"a", "an", "another", "abandon", "about", "adam", "boy", "body", "zoo"};
        final static String[] testVals =
            new String[]{"the first letter of English",
                         "used instead of 'a' when the following word begins witha vowel sound",
                         "one more person or thing or an extra amount",
                         "to leave a place, thing or person forever",
                         "on the subject of; connected with",
                         "a character in the Bible who was the first man made by God",
                         "a male child or, more generally, a male of any age",
                         "the whole physical structure that forms a person or animal",
                         "an area in which animals, especially wild animals, are kept so that people can go and look at them, or study them"};

        static void testEdict() {
            PrefixTree.Node<String> t = null;
            Map<String, String> m = new HashMap<>();
            int n = Math.min(testKeys.length, testVals.length);
            for (int i = 0; i < n; ++i) {
                t = PrefixTree.insert(t, testKeys[i], testVals[i]);
                m.put(testKeys[i], testVals[i]);
            }
            verifyLookup(m, t, "a", 5);
            verifyLookup(m, t, "a", 6);
            verifyLookup(m, t, "a", 7);
            verifyLookup(m, t, "ab", 2);
            verifyLookup(m, t, "ab", 5);
            verifyLookup(m, t, "b", 2);
            verifyLookup(m, t, "bo", 5);
            verifyLookup(m, t, "z", 3);
        }

        static void verifyLookup(Map<String, String> m, PrefixTree.Node<String> t, String key, int n) {
            System.out.format("lookup %s with limit: %d\n", key, n);
            SortedMap<String, String> m1 = new TreeMap<>();
            for (Map.Entry<String, String> e : lookup(t, key, n)) {
                m1.put(e.getKey(), e.getValue());
            }
            SortedMap<String, String> m2 =
                take(n, toSortedMap(m.entrySet().stream()
                                    .filter(e -> e.getKey().startsWith(key))));
            if (!m2.equals(m1))
                throw new RuntimeException("\n" + m1.toString() + "\n!=\n" + m2.toString());

            System.out.println("result:\n" + m1.toString());
        }

        static <T> SortedMap<String, T> take(int n, SortedMap<String, T> m) {
            return toSortedMap(m.entrySet().stream().limit(n));
        }

        static <K, V> SortedMap<K, V> toSortedMap(Stream<Map.Entry<K, V>> s) {
            return new TreeMap<>(s.collect(Collectors.toMap(e -> e.getKey(), e ->e.getValue())));
        }

        public static void testT9() {
            System.out.println("T9 map: " + mapT9);
            System.out.println("reverse T9 map: " + rmapT9);
        }

        public static void test() {
            testEdict();
            testT9();
        }
    }
}
