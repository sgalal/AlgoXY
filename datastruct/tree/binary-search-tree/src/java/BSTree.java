import java.util.*;
import java.util.function.*;

public class BSTree {

    public static class Node<T> {
        public T key;
        public Node<T> left;
        public Node<T> right;
        public Node<T> parent; //optional, mainly used for succ/pred.

        public Node(T x) {
            key = x;
        }
    }

    // in order traverse
    public static <T> void traverse(Node<T> t, Consumer<T> f) {
        if (t != null) {
            traverse(t.left, f);
            f.accept(t.key);
            traverse(t.right, f);
        }
    }

    public static <T extends Comparable<? super T>> Node<T> insert(Node<T> tr, T key) {
        Node<T> root = tr, x = new Node<>(key), parent = null;
        while (tr != null) {
            parent = tr;
            tr = (key.compareTo(tr.key) < 0) ? tr.left : tr.right;
        }
        x.parent = parent;
        if (parent == null) // tree is empty
            return x;
        else if (key.compareTo(parent.key) < 0)
            parent.left = x;
        else
            parent.right = x;
        return root;
    }

    public static <T extends Comparable<? super T>> Node<T> search(Node<T> tr, T x) {
        while (tr != null && !tr.key.equals(x))
            tr = (x.compareTo(tr.key) < 0) ? tr.left : tr.right;
        return tr;
    }

    // auxiliary utilities
    public static <T extends Comparable<? super T>> Node<T> fromList(Collection<T> xs) {
        Node<T> t = null;
        for (T x : xs)
            t = insert(t, x);
        return t;
    }

    public static <T> List<T> toList(Node<T> t) {
        if (t == null) return new ArrayList<T>();
        final List<T> xs = toList(t.left);
        xs.add(t.key);
        xs.addAll(toList(t.right));
        return xs;
    }
}
