import java.util.*;
import java.util.stream.*;
import java.lang.Exception;

public class TestUtil {
    static List<Integer> genList(Random gen, int maxLen) {
        final List<Integer> xs = IntStream.range(0, maxLen).boxed().collect(Collectors.toList());
        Collections.shuffle(xs);
        return xs.subList(0, gen.nextInt(maxLen));
    }

    static <T> String join(Collection<T> xs) {
        return xs.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    static <T> void assertEq(List<T> xs, List<T> ys) {
        if (xs.size() != ys.size() || !xs.equals(ys))

            throw new RuntimeException(String.format("[%s] != [%s]", join(xs), join(ys)));
    }
}
