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

    public static void main(String[] args) {
        final Random gen = new Random();
        for (int i = 0; i < 100; ++i) {
            final List<Integer> xs = genList(gen);
            testBuild(xs);
            testSearch(xs, gen.nextInt(N));
        }
        System.out.println("passed 100 tests.");
        traverse(fromList(genList(gen)), System.out::println);
    }
}
