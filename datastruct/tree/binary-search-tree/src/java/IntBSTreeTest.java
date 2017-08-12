import java.util.*;
import java.util.stream.*;
import java.lang.Exception;

public class IntBSTreeTest extends IntBSTree {
    final static int N = 100;

    static void testBuild(List<Integer> xs) {
        TestUtil.assertEq(toList(fromList(xs)), xs.stream().sorted().collect(Collectors.toList()));
    }

    static void testSearch(List<Integer> xs, int x) {
        final Node tr = search(IntBSTree.fromList(xs), x);
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
        for (int i = 0; i < N; ++i) {
            final List<Integer> xs = TestUtil.genList(gen, N);
            testBuild(xs);
            testSearch(xs, gen.nextInt(N));
            testMinMax(xs);
            testSuccPred(xs);
        }
        System.out.format("passed %d tests.\n", N);
        //traverse(fromList(genList(gen)), System.out::println);
    }
}
