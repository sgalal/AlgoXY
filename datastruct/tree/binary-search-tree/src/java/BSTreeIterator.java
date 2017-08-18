import java.util.*;
import java.util.stream.*;
import java.lang.Exception;

public class BSTreeIterator<T> extends BSTree {
    private Node<T> node;

    public BSTreeIterator(Node<T> root) {
        node = min(root);
    }

    // throws NullPointerException if tree is empty or exceeds begin/end positions.
    T getKey() {
        return node.key;
    }

    public boolean hasNext() {
        return node != null;
    }

    public BSTreeIterator<T> next() {
        if (hasNext())
            node = succ(node);
        return this;
    }

    // verification
    static final int N = 100;

    public static void main(String[] args) {
        final Random gen = new Random();
        for (int i = 0; i < 100; ++i) {
            final List<Integer> xs = TestUtil.genList(gen, N);
            final Node<Integer> tr = fromList(xs);
            List<Integer> ys = new ArrayList<Integer>();
            for (BSTreeIterator<Integer> it = new BSTreeIterator<>(tr);
                 it.hasNext(); it.next())
                ys.add(it.getKey());
            TestUtil.assertEq(xs.stream().sorted().collect(Collectors.toList()), ys);

        }
        System.out.println("passed 100 tests.");
    }

}
