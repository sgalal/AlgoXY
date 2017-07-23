import java.util.*;

public class Node<T> {
    public T key;
    public Node left;
    public Node right;
    public Node parent; //optional, mainly used for succ/pred.

    public Node(T x) {
        key = x;
    }
}
