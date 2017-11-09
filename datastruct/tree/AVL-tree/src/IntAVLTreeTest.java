import java.util.*;
import java.util.stream.*;
import static java.lang.Math.*;
import java.lang.Exception;

public class IntAVLTreeTest extends IntAVLTree {
    static boolean isAVL(Node t) {
        if (t == null) return true;
        int delta = height(t.right) - height(t.left);
        return isAVL(t.left) && isAVL(t.right) && abs(delta) <= 1;
    }

    static void verifyBST(Node t, List<Integer> xs) {
        if(!toList(t).equals(xs.stream().sorted().collect(Collectors.toList())))
            throw new RuntimeException(String.format("tree %s isn't BST of [%s]",
                toStr(t),
                xs.stream().map(Object::toString).collect(Collectors.joining(", "))));
    }

    static List<Integer> genList(Random gen, int maxLen) {
        List<Integer> xs = IntStream.range(0, maxLen).boxed().collect(Collectors.toList());
        Collections.shuffle(xs);
        return xs.subList(0, gen.nextInt(maxLen));
    }

    public static void testInsert(List<Integer> xs) {
        Node t = fromList(xs);
        verifyBST(t, xs);
        if (!isAVL(t))
            throw new RuntimeException(String.format("violate AVL properties: %s", toStr(t)));
    }

    final static int N = 100;

    public static void main(String[] args) {
        Random gen = new Random();
        for (int i = 0; i < N; ++i) {
            List<Integer> xs = genList(gen, N);
            testInsert(xs);
        }
        System.out.format("%d test passed.\n", N);
    }
}
