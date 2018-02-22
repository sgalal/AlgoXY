import java.util.*;
import java.util.stream.*;
import java.lang.Exception;

public class PrefixTreeApp {
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
            for (Map.Entry<String, PrefixTree.Node<T>> e : tr.subTrees.entrySet()) {
                q.offer(entryOf(s + e.getKey(), e.getValue()));
            }
        }
        return res;
    }

    static <K, V> Map.Entry<K, V> entryOf(K key, V val) {
        return new AbstractMap.SimpleImmutableEntry<K, V>(key, val);
    }
}
