import java.util.*;
import java.util.stream.*;
import java.lang.Exception;

public class BSTreeTest extends BSTree {
    final static int N = 100;

    static void testBuild(List<Integer> xs) {
        TestUtil.assertEq(toList(fromList(xs)), xs.stream().sorted().collect(Collectors.toList()));
    }

    static void testSearch(List<Integer> xs, int x) {
        final Node<Integer> tr = search(fromList(xs), x);
        if (tr == null) {
            if (xs.contains(x))
                throw new RuntimeException(String.format("%d exits", x));
        } else {
            if (!tr.key.equals(x))
                throw new RuntimeException(String.format("given %d, found %d", x, tr.key));
        }
    }

    static void testMinMax(List<Integer> xs) {
        final Node<Integer> t = fromList(xs);
        if (t == null) return;
        if (!Collections.min(xs).equals(min(t).key))
            throw new RuntimeException(String.format("min(xs)=%d, min(tree)=%d",
                                                 Collections.min(xs), min(t).key));
        if (!Collections.max(xs).equals(max(t).key))
            throw new RuntimeException(String.format("max(xs)=%d, min(tree)=%d",
                                                 Collections.max(xs), max(t).key));
    }

    static void testSuccPred(List<Integer> xs) {
        if (xs.isEmpty()) return;
        final Node<Integer> t = fromList(xs);
        List<Integer> ys = xs.stream().sorted().collect(Collectors.toList());
        List<Integer> zs = new ArrayList<>();
        Node<Integer> p = search(t, ys.get(0));
        while (p != null) {
            zs.add(p.key);
            p = succ(p);
        }
        TestUtil.assertEq(ys, zs);

        p = search(t, ys.get(ys.size() - 1));
        zs.clear();
        while (p != null) {
            zs.add(p.key);
            p = pred(p);
        }
        Collections.reverse(zs);
        TestUtil.assertEq(ys, zs);
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
