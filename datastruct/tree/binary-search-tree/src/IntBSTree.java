import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.lang.Exception;

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

    // verification

    static Node fromList(Collection<Integer> xs) {
        Node t = null;
        for (Integer x : xs)
            t = insert(t, x);
        return t;
    }

    static List<Integer> toList(Node t) {
        if (t == null) return new ArrayList<Integer>();
        List<Integer> xs = toList(t.left);
        xs.add(t.key);
        xs.addAll(toList(t.right));
        return xs;
    }

    static final int N = 100;

    static List<Integer> genList(Random gen) {
        List<Integer> xs = IntStream.range(0, N).boxed().collect(Collectors.toList());
        Collections.shuffle(xs);
        return xs.subList(0, gen.nextInt(N));
    }

    static <T> String join(Collection<T> xs) {
        return xs.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    static <T> void assertEq(List<T> xs, List<T> ys) {
        if (xs.size() != ys.size() || !xs.equals(ys)) {
            System.out.format("[%s] != [%y]", join(xs), join(ys));
            throw new RuntimeException("length compare fail");
        }
    }

    static void testBuild(List<Integer> xs) {
        assertEq(toList(fromList(xs)), xs.stream().sorted().collect(Collectors.toList()));
    }

    static void testSearch(List<Integer> xs, int x) {
        final Node tr = search(fromList(xs), x);
        if (tr == null) {
            if (xs.contains(x))
                throw new RuntimeException(String.format("%d exits", x));
        } else {
            if (tr.key != x)
                throw new RuntimeException(String.format("given %d, found %d", x, tr.key));
        }
    }

    static void testMinMax(List<Integer> xs) {
        final Node t = fromList(xs);
        if (t == null) return;
        if (!Collections.min(xs).equals(min(t).key))
            throw new RuntimeException(String.format("min(xs)=%d, min(tree)=%d",
                                                 Collections.min(xs), min(t).key));
        if (!Collections.max(xs).equals(max(t).key))
            throw new RuntimeException(String.format("max(xs)=%d, min(tree)=%d",
                                                 Collections.max(xs), max(t).key));
    }

    static void testSuccPred(List<Integer> xs) {
        final Node t = fromList(xs);
        List<Integer> ys = xs.stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < xs.size(); ++i) {
            Node x = search(t, ys.get(i));
            Node y = succ(x), z = pred(x);
            if (i == 0 && z != null)
                throw new RuntimeException("succ(first) != null");
            if (i == xs.size() - 1 && y != null)
                throw new RuntimeException("pred(last) != null");
            if (i > 0 && z.key != ys.get(i - 1))
                throw new RuntimeException(String.format("pred(%d) = %d != %d", x.key, z.key, ys.get(i - 1)));
            if (i != xs.size() - 1 && y.key != ys.get(i + 1))
                throw new RuntimeException(String.format("succ(%d) = %d != %d", x.key, y.key, ys.get(i + 1)));
        }
    }

    public static void main(String[] args) {
        final Random gen = new Random();
        for (int i = 0; i < 100; ++i) {
            final List<Integer> xs = genList(gen);
            testBuild(xs);
            testSearch(xs, gen.nextInt(N));
            testMinMax(xs);
            testSuccPred(xs);
        }
        System.out.println("passed 100 tests.");
        traverse(fromList(genList(gen)), System.out::println);
    }
}
