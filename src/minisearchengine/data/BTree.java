package minisearchengine.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class BTree<T extends Comparable<T>> {

    public static final int MAX_NUMBER_OF_NODES = 3;
    public static final int MAX_CHILDREN_COUNT = 4;

    protected BTreeNode<T> root;

    public BTree() {
        this.root = new BTreeNode<>();
        this.root.leaf = true;
        this.root.numberOfNodes = 0;
        this.root.keys.set(0, null);
    }

    /**
     * Inserts given value into the tree
     * @param value Value to be inserted
     */
    public void insert(T value) {
        BTreeNode<T> oldRoot = this.root;

        if (oldRoot.numberOfNodes == MAX_NUMBER_OF_NODES) {
            BTreeNode<T> node = new BTreeNode<>();
            this.root = node;
            node.numberOfNodes = 0;
            node.leaf = false;
            node.children.set(0, oldRoot);

            splitChild(node, 1, oldRoot);
            insertNonfull(node, value);

        } else {
            insertNonfull(oldRoot, value);
        }
    }

    private void insertNonfull(BTreeNode<T> node, T value) {
        int i = node.numberOfNodes;

        if (node.leaf) {
            while (i >= 1 && compareValues(value, node.keys.get(i - 1)) < 0) {
                node.keys.set(i, node.keys.get(i - 1));
                i--;
            }
            node.keys.set(i, value);
            node.numberOfNodes++;

        } else {
            while (i >= 1 && compareValues(value, node.keys.get(i - 1)) < 0) {
                i--;
            }
            i++;
            if (node.children.get(i - 1).numberOfNodes == MAX_NUMBER_OF_NODES) {
                splitChild(node, i, node.children.get(i - 1));
                if (compareValues(value, node.keys.get(i - 1)) > 0) {
                    i++;
                }
            }
            insertNonfull(node.children.get(i - 1), value);
        }
    }

    private void splitChild(BTreeNode<T> parentNode, int childIndex, BTreeNode<T> newChild) {
        BTreeNode<T> z = new BTreeNode<>();
        z.leaf = newChild.leaf;
        z.numberOfNodes = 1;
        z.children.set(0, newChild.children.get(2));

        if (!newChild.leaf) {
            z.children.set(1, newChild.children.get(3));
            z.children.set(0, newChild.children.get(2));
        }
        newChild.numberOfNodes = 1;

        for (int i = parentNode.numberOfNodes + 1; i >= childIndex + 1; i--) {
            parentNode.children.set(i, parentNode.children.get(i - 1));
            parentNode.keys.set(i - 1, parentNode.keys.get(i - 2));
        }

        parentNode.children.set(childIndex, z);
        parentNode.keys.set(childIndex - 1, newChild.keys.get(1));
        parentNode.numberOfNodes++;
    }

    /**
     * Searches given value in the tree.
     * @param value Value to be searched
     * @return Returns whether given value exists in the tree or not
     */
    public boolean search(T value) {
        return search(root, value);
    }

    private boolean search(BTreeNode<T> node, T value) {
        int i = 1;

        while (i <= node.numberOfNodes && compareValues(value, node.keys.get(i - 1)) > 0) {
            i++;
        }

        if (i <= node.numberOfNodes && value.equals(node.keys.get(i - 1))) {
            return true;
        }

        if (!node.leaf) {
            return search(node.children.get(i - 1), value);
        }

        return false;
    }

    /**
     * Prints the tree in a readable format on the console.
     * Used only for debugging purposes
     */
    public void print() {
        printBtree(root, "");
    }

    private void printBtree(BTreeNode<T> node, String indent) {
        if (node == null) {
            System.out.println(indent + "");

        } else {
            System.out.println(indent + " ");

            String childIndent = indent + "\t";

            for (int i = node.numberOfNodes - 1; i >= 0; i--) {
                if (!node.leaf) printBtree(node.children.get(i), childIndent);
                if (node.keys.get(i) != null)
                    System.out.println(childIndent + node.keys.get(i));
            }

            if (!node.leaf) {
                printBtree(node.children.get(node.numberOfNodes), childIndent);
            }
        }
    }

    /**
     * Traverses each node in the tree.
     * And invokes given consumer with each node.
     * @param consumer Consumer to be used on each node traversal
     */
    public void traverse(Consumer<T> consumer) {
        traverse(root, consumer);
    }

    private void traverse(BTreeNode<T> node, Consumer<T> consumer) {
        if (node != null) {
            for (int i = node.numberOfNodes - 1; i >= 0; i--) {
                if (!node.leaf) traverse(node.children.get(i), consumer);
                if (node.keys.get(i) != null)
                    consumer.accept(node.keys.get(i));
            }

            if (!node.leaf) {
                traverse(node.children.get(node.numberOfNodes), consumer);
            }
        }
    }

    private int compareValues(T value1, T value2) {
        if (value1 == null) return -1;
        if (value2 == null) return -1;
        return value1.compareTo(value2);
    }

    public static class BTreeNode<T extends Comparable<T>> {
        int numberOfNodes;
        ArrayList<T> keys;
        ArrayList<BTreeNode<T>> children;
        boolean leaf;

        public BTreeNode() {
            keys = new ArrayList<>(MAX_NUMBER_OF_NODES);
            for (int i = 0; i < MAX_NUMBER_OF_NODES; i++) {
                keys.add(null);
            }
            children = new ArrayList<>(MAX_CHILDREN_COUNT);
            for (int i = 0; i < MAX_CHILDREN_COUNT; i++) {
                children.add(null);
            }
        }
    }

}
