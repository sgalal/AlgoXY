import java.util.*;
import static java.lang.Math.*;

/* AVL tree, with integral key for illustration purpose. */

public class IntAVLTree {
    public static class Node {
        public int key;
        public int delta;  // balance factor
        public Node left;
        public Node right;
        public Node parent;

        public Node(int k) { Key = k; }

        void setLeft(Node x) {
            left = x;
            if (x != null) x.parent = this;
        }

        void setRight(Node x) {
            right = x;
            if (x != null) x.parent = this;
        }

        void setChildren(Node x, Node y) {
            setLeft(x);
            setRight(y);
        }

        void replaceWith(Node y) {
            replace(parent, this, y);
        }
    }

    // change from: parent --> x to parent --> y
    public static Node replace(Node parent, Node x, Node y) {
        if (parent == null) {
            if (y != null) y.parent = null;
        } else if (parent.left == x) {
            parent.setLeft(y);
        } else {
            parent.setRight(y);
        }
        if (x != null) x.parent = null;
        return y;
    }

    // Rotation. It doesn't change the delta

    // rotate left (a, x, (b, y, c)) ==> ((a, x, b), y, c)
    public static Node rotateLeft(Node t, Node x) {
        Node parent = x.parent;
        Node y = x.right;
        Node a = x.left;
        Node b = y.left;
        Node c = y.right;
        x.replaceWith(y);
        x.setChildren(a, b);
        y.setChildren(x, c);
        if (parent == null) t = y;
        return t;
    }

    // rotate right: (a, x, (b, y, c) <== ((a, x, b), y, c)
    public static Node rotateRight(Node t, Node y) {
        Node parent = y.parent;
        Node x = y.left;
        Node a = x.left;
        Node b = x.right;
        Node c = y.right;
        y.replaceWith(x);
        y.setChildren(b, c);
        x.setChildren(a, y);
        if (parent == null) t = x;
        return t;
    }

    // top-down insert, returns the new root
    public static Node insert(Node t, int key) {
        Node root = t;
        Node x = new Node(key);
        Node parent = null;
        while (t != null) {
            parent = t;
            t = key < t.key ? t.left : t.right;
        }
        if (parent == null) { // insert key to the empty tree
            root = x;
        } else if (key < parent.key) {
            parent.setLeft(x);
        } else {
            parent.setRight(x);
        }
        return insertFix(root, x);
    }

    /*
     * bottom-up update delta and fix
     *   t: tree root;
     *   x: the sub-tree that the height increases.
     */
    private static Node insertFix(Node t, Node x) {
        /*
         * denote d = delta(t), d' = delta(t'),
         *   where t' is the new tree after insertion.
         *
         * case 1: |d| == 0, |d'| == 1, height increase,
         *    we need go on bottom-up updating.
         *
         * case 2: |d| == 1, |d'| == 0, height doesn't change,
         *    program terminate
         *
         * case 3: |d| == 1, |d'| == 2, AVL violation,
         *    we need fixing by rotation.
         */
        while (x.parent != null) {
            int d1 = x.parent.delta;
            int d2 = d1 + (x == x.parent.left ? -1 : 1);
            x.parent.delta = d2;
            Node p = x.parent;
            Node l = x.parent.left;
            Node r = x.parent.right;
            if (abs(d1) == 1 && abs(d2) == 0) {
                return t;
            } else if (abs(d1) == 0 && abs(d2) == 1) {
                x = x.parent;
            } else if (abs(d1) == 1 && abs(d2) == 2) {
                if (d2 == 2) {
                } else if (d2 == -2) {
                }
                break;
            } else {
                throw new RuntimeException(String.format(
                    "shouldn't be here, d1 = %d, d2 = %d", d1, d2));
            }
        }
        return t;
    }
}
