import java.util.*;
import java.util.stream.*;
import java.lang.Exception;

public class BSTreeIterator<T> {
    public static class Node<T> {
        public T key;
        public Node<T> left;
        public Node<T> right;
        public Node<T> parent;

        public Node(T x) {
            key = x;
        }
    }

    private Node<T> node;

    public BSTreeIterator(Node<T> root) {
        node = min(root);
    }

    private Node<T> min(Node<T> tr) {
        while (tr != null && tr.left != null)
            tr = tr.left;
        return tr;
    }

    // throws NullPointerException if tree is empty or exceeds begin/end positions.
    T getKey() {
        return node.key;
    }

    public boolean hasNext() {
        return node != null;
    }

    private Node<T> next(Node<T> node) {
        if (node == null) return node;
        if (node.right != null)
            return min(node.right);
        else {
            Node<T> parent = node.parent;
            while (parent != null && parent.right == node) {
                node = parent;
                parent = node.parent;
            }
            return parent;
        }
    }

    public BSTreeIterator<T> next() {
        node = next(node);
        return this;
    }

    // verification

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


    static <T extends Comparable<? super T>> Node<T> fromList(Collection<T> xs) {
        Node<T> t = null;
        for (T x : xs)
            t = insert(t, x);
        return t;
    }

    static <T> List<T> toList(Node<T> t) {
        if (t == null) return new ArrayList<T>();
        final List<T> xs = toList(t.left);
        xs.add(t.key);
        xs.addAll(toList(t.right));
        return xs;
    }

    static final int N = 100;

    static List<Integer> genList(Random gen) {
        final List<Integer> xs = IntStream.range(0, N).boxed().collect(Collectors.toList());
        Collections.shuffle(xs);
        return xs.subList(0, gen.nextInt(N));
    }

    static <T> String join(Collection<T> xs) {
        return xs.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    static <T> void assertEq(List<T> xs, List<T> ys) {
        if (xs.size() != ys.size() || !xs.equals(ys)) {
            System.out.format("[%s] != [%s]", join(xs), join(ys));
            throw new RuntimeException("length compare fail");
        }
    }

    public static void main(String[] args) {
        final Random gen = new Random();
        for (int i = 0; i < 100; ++i) {
            final List<Integer> xs = genList(gen);
            final Node<Integer> tr = fromList(xs);
            List<Integer> ys = new ArrayList<Integer>();
            for (BSTreeIterator<Integer> it = new BSTreeIterator<>(tr);
                 it.hasNext(); it.next())
                ys.add(it.getKey());
            assertEq(xs.stream().sorted().collect(Collectors.toList()), ys);

        }
        System.out.println("passed 100 tests.");
    }

}
