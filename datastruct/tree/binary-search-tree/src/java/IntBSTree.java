import java.util.*;
import java.util.function.*;

/* For illustration purpose, integer based BST needn't deal with comparable interface */
public class IntBSTree {

    public static class Node {
        public int key;
        public Node left;
        public Node right;
        public Node parent; //optional, mainly used for succ/pred.

        public Node(int x) {
            key = x;
        }
    }

    // in order traverse
    public static void traverse(Node t, Consumer<Integer> f) {
        if (t != null) {
            traverse(t.left, f);
            f.accept(t.key);
            traverse(t.right, f);
        }
    }

    public static Node insert(Node tr, int key) {
        Node root = tr, x = new Node(key), parent = null;
        while (tr != null) {
            parent = tr;
            tr = (key < tr.key) ? tr.left : tr.right;
        }
        x.parent = parent;
        if (parent == null) // tree is empty
            return x;
        else if (key < parent.key)
            parent.left = x;
        else
            parent.right = x;
        return root;
    }

    public static Node search(Node tr, int x) {
        while (tr != null && tr.key != x)
            tr = x < tr.key ? tr.left : tr.right;
        return tr;
    }

    public static Node min(Node t) {
        while (t != null && t.left != null)
            t = t.left;
        return t;
    }

    public static Node max(Node t) {
        while (t != null && t.right != null)
            t = t.right;
        return t;
    }

    // assume the input x isn't null when find succ/pred

    public static Node succ(Node x) {
        if (x.right != null)
            return min(x.right);
        Node p = x.parent;
        while (p != null && p.left != x) {
            x = p;
            p = p.parent;
        }
        return p;
    }

    public static Node pred(Node x) {
        if (x.left != null)
            return max(x.left);
        Node p = x.parent;
        while (p != null && p.right != x) {
            x = p;
            p = p.parent;
        }
        return p;
    }

    // Auxiliary

    public static Node fromList(Collection<Integer> xs) {
        Node t = null;
        for (Integer x : xs)
            t = insert(t, x);
        return t;
    }

    public static List<Integer> toList(Node t) {
        if (t == null) return new ArrayList<Integer>();
        List<Integer> xs = toList(t.left);
        xs.add(t.key);
        xs.addAll(toList(t.right));
        return xs;
    }
}
