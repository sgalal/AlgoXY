import java.util.*;
import java.util.stream.*;
import java.lang.Exception;

public class InsertSort {
    static <T> void swap(T[] xs, int i, int j) {
        T tmp = xs[i];
        xs[i] = xs[j];
        xs[j] = tmp;
    }

    /* insert x[i] to xs[0...i-1] */
    static int[] sort(int[] xs) {
        for(int i = 1; i < xs.length; ++i) {
            int x = xs[i], j = i - 1;
            while(j >=0 && x < xs[j])
                xs[j+1] = xs[j--];
            xs[j+1] = x;
        }
        return xs;
    }

    /* verification and auxiliary functions */

    static List<Integer> genList(Random gen, int n) {
        List<Integer> xs = IntStream.range(0, n).boxed().collect(Collectors.toList());
        Collections.shuffle(xs);
        return xs.subList(0, gen.nextInt(n));
    }

    static int[] fromList(List<Integer> xs) {
        return xs.stream().mapToInt(Integer::intValue).toArray();
    }

    static List<Integer> toList(int[] xs) {
        return IntStream.of(xs).boxed().collect(Collectors.toList());
    }

    static <T> String join(Collection<T> xs) {
        return xs.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    static <T> void assertEq(List<T> xs, List<T> ys) {
        if (xs.size() != ys.size() || !xs.equals(ys))
            throw new RuntimeException(String.format("[%s] != [%s]", join(xs), join(ys)));
    }

    final static int N = 100;

    public static void main(String[] args) {
        Random gen = new Random();
        for (int i = 0; i < N; ++i) {
            List<Integer> xs = genList(gen, N);
            List<Integer> ys = xs.stream().sorted().collect(Collectors.toList());
            List<Integer> zs = toList(sort(fromList(xs)));
            assertEq(ys, zs);
        }
        System.out.format("passed %d tests\n", N);
    }
}
