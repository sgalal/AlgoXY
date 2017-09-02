import java.util.*;
import java.util.stream.*;
import java.lang.Exception;

public class InsertSort {

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

    /* An equivalent insertion sort, but need more operations. */
    static int[] isort(int[] xs) {
        int i, j;
        for (i = 1; i < xs.length; ++i)
            for (j = i - 1; j >= 0 && xs[j + 1] < xs[j]; --j)
                swap(xs, j, j + 1);
        return xs;
    }

    static void swap(int[] xs, int i, int j) {
        int tmp = xs[i];
        xs[i] = xs[j];
        xs[j] = tmp;
    }

    /* Using binary search to locate the insert position */
    static int[] insertSort(int[] xs) {
        for(int i = 1; i < xs.length; ++i) {
            int x = xs[i];
            int p = binarySearch(xs, i, x);
            for (int j = i; j > p; --j)
                xs[j] = xs[j - 1];
            xs[p] = x;
        }
        return xs;
    }

    static int binarySearch(int[] xs, int u, int x) {
        int l = 0;
        while (l < u) {
            int m = l + (u - l) / 2;    // essentially: (l + u) / 2
            if (xs[m] == x) return m;
            else if (xs[m] < x) l = m + 1;
            else u = m;
        }
        return l;
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
            zs = toList(isort(fromList(xs)));
            assertEq(ys, zs);
            zs = toList(insertSort(fromList(xs)));
            assertEq(ys, zs);
        }
        System.out.format("passed %d tests\n", N);
    }
}
