import java.util.*;

/**
 * Implementation of a BST based String Set.
 * @author Jessica Yang
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        Node last = find(s);
        if (last == null) {
            _root = new Node(s);
        } else {
            int c = s.compareTo(last.s);
            if (c < 0) {
                last.left = new Node(s);
            } else if (c > 0) {
                last.right = new Node(s);
            }
        }
    }

    @Override
    public boolean contains(String s) {
        Node last = find(s);
        return last != null && s.equals(last.s);
    }

    private Node find(String s) {
        if (_root == null) {
            return null;
        }
        Node p;
        p = _root;
        while (true) {
            int c = s.compareTo(p.s);
            Node next;
            if (c < 0) {
                next = p.left;
            } else if (c > 0) {
                next = p.right;
            } else {
                return p;
            }
            if (next == null) {
                return p;
            } else {
                p = next;
            }
        }
    }

    @Override
    public List<String> asList() {
        BSTIterator bst = new BSTIterator(_root);
        List<String> ar = new ArrayList<>();
        while (bst.hasNext()) {
            ar.add(bst.next());
        }
        return ar;
    }



    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    /** An iterator over a BSTStringSet with bounds. */
    private static class BSTIteratorRange implements Iterator<String> {

        private Stack<Node> _toDo = new Stack<>();
        private String _low;
        private String _high;

        BSTIteratorRange(Node node, String low, String high) {
            _low = low;
            _high = high;
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node node = _toDo.pop();
            return node.s;
        }

        private void addTree(Node n) {
            if (n == null) {
                return;
            }
            else if (n.s.compareTo(_low) < 0) {
                addTree(n.right);
            } else if (n.s.compareTo(_high) > 0) {
                addTree(n.left);
            } else {
                addTree(n.right);
                _toDo.push(n);
                addTree(n.left);
            }
        }

    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }


    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTIteratorRange(_root, low, high);
    }


    /** Root node of the tree. */
    private Node _root;
}
