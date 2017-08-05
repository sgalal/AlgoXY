import java.util.*;
import java.util.stream.*;
import java.lang.Exception;
import static TestUtil.*;
import sttic BSTree.*;

class BSTreeTest {
    final static int N = 100;

    static void testBuild(List<Integer> xs) {
        assertEq(BStree.toList(fromList(xs)), xs.stream().sorted().collect(Collectors.toList()));
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

    public static void main(String[] args) {
        final Random gen = new Random();
        for (int i = 0; i < N; ++i) {
            final List<Integer> xs = genList(gen);
            testBuild(xs);
            testSearch(xs, gen.nextInt(N));
        }
        System.out.format("passed %d tests.\n", N);
        //traverse(fromList(genList(gen)), System.out::println);
    }
}
