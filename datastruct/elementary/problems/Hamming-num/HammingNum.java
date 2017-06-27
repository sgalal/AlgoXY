/**
 * Hamming Number
 */

import java.lang.*;
import java.util.*;
import java.util.stream.*;

class HammingNum {
    static String join(long[] xs) {
        return LongStream.of(xs).boxed().map(Object::toString)
            .collect(Collectors.joining(", "));
    }

    static long min(long a, long b, long c) {
        return Math.min(a, Math.min(b, c));
    }

    public static long get(int m) {
        long nums[] = new long[m + 1];
        int n = 0, i = 0, j = 0, k = 0;
        nums[0] = 1;
        long x2 = 2 * nums[i];
        long x3 = 3 * nums[j];
        long x5 = 5 * nums[k];
        while (n < m) {
            nums[++n] = min(x2, x3, x5);
            if (x2 == nums[n]) x2 = 2 * nums[++i];
            if (x3 == nums[n]) x3 = 3 * nums[++j];
            if (x5 == nums[n]) x5 = 5 * nums[++k];
        }
        return nums[n - 1];
    }

    public static void main(String[] args) {
        System.out.println(get(1500));
    }
}
