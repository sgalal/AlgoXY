import java.util.*;
import java.lang.Exception;

public class IntRBTreeTest extends IntRBTree {

    Node t1, t2;

    private static Color colorOf(char c) {
        switch (c) {
        case 'R':
            return Color.RED;
        case 'B':
            return Color.BLACK;
        default:
            return Color.DOUBLY_BLACK;
        }
    }

    private static Node nodeOf(int x, char c) {
        return new Node(x, colorOf(c));
    }

    private static Node tr(Node l, int x, char c, Node r) {
        Node t = nodeOf(x, c);
        t.setChildren(l, r);
        return t;
    }

    public IntRBTreeTest() {
        // t1 = ((1:B, 2:R, (3:R, 4:B, .)), 5:B, (6:B, 7:R, (8:R, 9:B, .)))
        t1 = tr(tr(nodeOf(1, 'B'), 2, 'R', tr(nodeOf(3, 'R'), 4, 'B', null)),
                   5, 'B',
                   tr(nodeOf(6, 'B'), 7, 'R', tr(nodeOf(8, 'R'), 9, 'B', null)));
        System.out.format("t1 1..9\n%s\n", toStr(t1));

        /*
         * t2 as figure 13.4 in CLRS
         * (((. 1:B .) 2:R ((. 5:R .) 7:B (. 8:R .))) 11:B (. 14:B (. 15:R .)))
         */
        t2 = tr(tr(nodeOf(1, 'B'), 2, 'R', tr(nodeOf(5, 'R'), 7, 'B', nodeOf(8, 'R'))),
                11, 'B',
                tr(null, 14, 'B', nodeOf(15, 'R')));
        System.out.format("t2, CLRS fig 13.4\n%s\n", toStr(t2));

    }

    static void assertEq(Node a, Node b) {
        String s1 = toStr(a);
        String s2 = toStr(b);
        if (!s1.equals(s2))
            throw new RuntimeException(String.format("%s != %s", s1, s2));
    }

    public void testRotate() {
        Node t = clone(t1);
        Node x = t.right;  // 7:R
        t = rotateLeft(t, x); // (6 7 (8 9 .)) ==> ((6 7 8) 9 .)
        System.out.format("rotate left at 7:R\n%s\n", toStr(t));
        t = rotateRight(t, t.right); // rotate back
        System.out.format("rotate right back:\n%s\n", toStr(t));
        assertEq(t, t1);
    }

    public void testInsert() {
    }

    public void testDelete() {
    }

    public void run() {
        testRotate();
        testInsert();
        testDelete();
    }

    public static void main(String[] args) {
        (new IntRBTreeTest()).run();
    }
}
