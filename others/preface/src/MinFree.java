import java.util.*;
import java.util.stream.*;

/*
 * The puzzle:
 *   Given a list of non-negative numbers, find the minimum free number,
 *   Which is the minimum one not in this list.
 *
 * Except the brute-force one, all the methods are based on the fact:
 *     answer <= n
 * where n is the length of the array
 */
public class MinFree {

    /* Brute force method */
    static int findMinFree1(int[] nums) {
        int i;
        for(i = 0; contains(i, nums); i++) {}
        return i;
    }

    static boolean contains(int x, int[] nums) {
        for (int n : nums)
            if (x == n) return true;
        return false;
    }

    /* Flag array method */
    static int findMinFree2(int[] nums) {
        int i, n = nums.length;
        boolean[] flags = new boolean[n + 1];
        for (int x : nums)
            if (x <= n)
                flags[x] = true;
        for (i = 0; i < n && flags[i]; i++){}
        return i;
    }

    /* bit-wise flag array */
    final static int N = 1000;

    final static BitSet FLAGS = new BitSet(N);

    static int findMinFree3(int[] nums) {
        int i;
        FLAGS.clear();
        for (int x : nums)
            if (x <= nums.length)
                FLAGS.set(x);
        for (i = 0; i < nums.length && FLAGS.get(i); i++) {}
        return i;
    }

    static int[] fromList(List<Integer> xs) {
        return xs.stream().mapToInt(Integer::intValue).toArray();
    }

    static String join(int[] xs, String delim) {
        return IntStream.of(xs).boxed().map(Object::toString)
            .collect(Collectors.joining(delim));
    }

    public static void main(String[] args) {
        int k, m;
        List<Integer> xs = IntStream.range(0, N).boxed().collect(Collectors.toList());
        Random gen = new Random();
        for (int i = 0; i < 100; i++) {
            Collections.shuffle(xs);
            int n = gen.nextInt(N);
            int[] nums = fromList(xs.subList(0, n));
            if ((k = findMinFree1(nums)) != (m = findMinFree2(nums))) {
                System.out.println(join(nums, ","));
                System.out.format("brute force: %d\tflag array: %d\n", k, m);
                return;
            }
            if ((k = findMinFree3(nums)) != m) {
                System.out.println(join(nums, ","));
                System.out.format("bitset flag: %d\tflag array: %d\n", k, m);
                return;
            }
        }
        System.out.println("passed 100 tests");
    }
}
