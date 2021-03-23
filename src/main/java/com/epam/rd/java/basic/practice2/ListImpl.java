package com.epam.rd.java.basic.practice2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListImpl implements List {

    private Node firstNode;
    private Node lastNode;
    private boolean isEmpty;
    private int size;
    private static String excptnMsg = "No such element exception";

    public ListImpl() {
        this.firstNode = null;
        this.lastNode = null;
        isEmpty = true;
        size = 0;
    }

    public ListImpl(Object value) {
        this.firstNode = new Node(value);
        this.lastNode = this.firstNode;
        isEmpty = false;
        size = 1;
    }

    public static void main(String[] args) {
        List list = new ListImpl();
        list.addFirst("A");
        list.addLast("B");
        list.addLast("C");
        list.addFirst("X");
        list.addFirst("Y");
        list.addFirst("Z");
        list.addLast("D");
        list.addLast("E");
        System.out.println(list);
        list.removeFirst();
        list.removeFirst();
        list.removeFirst();
        list.remove("C");
        list.remove("A");
        list.remove("E");
        list.remove("D");
        list.remove("B");
        System.out.println(list);

        list.addFirst("A");
        list.addLast("B");
        list.addFirst("C");
        list.addFirst("D");
        Iterator it = list.iterator();
        System.out.println("Trying to remove without ever using next()");
        try {
            it.remove();
        } catch (IllegalStateException ex) {
            System.out.println(excptnMsg);
        }
        System.out.println("Removing first elements one by one: " + list);
        while (it.hasNext()) {
            System.out.println(it.next());
            it.remove();
        }
        System.out.println(list);
        System.out.println("Trying to remove element after already removing the next one");
        try {
            it.remove();
        } catch (IllegalStateException ex) {
            System.out.println(excptnMsg);
        }
    }

    @Override
    public void addFirst(Object element) {
        if (element != null) {
            if (isEmpty) {
                firstNode = new Node(null, element, null);
                lastNode = firstNode;
                isEmpty = false;
            } else {
                firstNode.setPrevNode(new Node(null, element, firstNode));
                firstNode = firstNode.prevNode;
            }
            size++;
        }
    }

    @Override
    public void addLast(Object element) {
        if (element != null) {
            if (isEmpty) {
                firstNode = new Node(null, element, null);
                lastNode = firstNode;
                isEmpty = false;
            } else {
                lastNode.setNextNode(new Node(lastNode, element, null));
                lastNode = lastNode.nextNode;
            }
            size++;
        }
    }

    @Override
    public void removeFirst() {
        firstNode = firstNode.nextNode;
        firstNode.setPrevNode(null);
        size--;
    }

    @Override
    public void removeLast() {
        lastNode = lastNode.prevNode;
        lastNode.setNextNode(null);
        size--;
    }

    @Override
    public Object getFirst() {
        return firstNode.getValue();
    }

    @Override
    public Object getLast() {
        return lastNode.getValue();
    }

    @Override
    public Object search(Object element) {
        if (element != null) {
            Node tempNode = firstNode;
            while (tempNode.nextNode != null) {
                if (tempNode.getValue().equals(element)) {
                    return tempNode.getValue();
                }
                tempNode = tempNode.nextNode;
            }
            if (tempNode.getValue().equals(element)) {
                return tempNode.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean remove(Object element) {
        if (element != null) {
            Node tempNode = firstNode;
            while (tempNode.nextNode != null) {
                if (tempNode.getValue().equals(element)) {
                    removeNode(tempNode);
                    return true;
                }
                tempNode = tempNode.nextNode;
            }
            if (tempNode.getValue().equals(element)) {
                removeNode(tempNode);
                return true;
            }
        }
        return false;
    }

    private Node removeNode(Node node) {
        if (node.prevNode == null) {
            if (node.nextNode == null) {
                node.value = null;
                isEmpty = true;
            } else {
                node = new Node(null, node.nextNode.value, node.nextNode.nextNode);
                if (node.nextNode != null) {
                    node.nextNode.setPrevNode(node);
                }
                firstNode = node;
            }
        } else {
            if (node.nextNode == null) {
                node = new Node(node.prevNode.prevNode, node.prevNode.value, null);
                lastNode = node;
            } else {
                node = new Node(node.prevNode, node.nextNode.value, node.nextNode.nextNode);
                node.nextNode.setPrevNode(node);
            }
            if (node.prevNode != null) {
                node.prevNode.setNextNode(node);
            } else {
                firstNode = node;
            }
        }
        size--;
        return node;
    }

    @Override
    public void clear() {
        this.firstNode = null;
        this.lastNode = null;
        isEmpty = true;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Object> iterator() {
        return new IteratorImpl();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node tempNode = firstNode;
        if (!isEmpty) {
            while (tempNode.nextNode != null) {
                result.append(tempNode.value).append(", ");
                tempNode = tempNode.nextNode;
            }
            result.append(tempNode.value);
        }
        return result.append("]").toString();
    }

    private static class Node {
        private Node prevNode;
        private Object value;
        private Node nextNode;

        public Node(Object value) {
            this.prevNode = null;
            this.value = value;
            this.nextNode = null;
        }

        public Node(Node prevNode, Object value, Node nextNode) {
            this.prevNode = prevNode;
            this.value = value;
            this.nextNode = nextNode;
        }

        public Node getPrevNode() {
            return prevNode;
        }

        public Object getValue() {
            return value;
        }

        public Node getNextNode() {
            return nextNode;
        }

        public void setPrevNode(Node prevNode) {
            this.prevNode = prevNode;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public void setNextNode(Node nextNode) {
            this.nextNode = nextNode;
        }
    }

    private class IteratorImpl implements Iterator {

        private Node currentNode;
        private boolean isNext;

        public IteratorImpl() {
            currentNode = firstNode;
            isNext = false;
        }

        @Override
        public boolean hasNext() {
            return ((isNext && currentNode.nextNode != null) || (!isNext && currentNode.getValue() != null));
        }

        @Override
        public Object next() {
            if (hasNext()) {
                if (isNext) {
                    currentNode = currentNode.getNextNode();
                } else {
                    isNext = true;
                }
                return currentNode.getValue();
            } else {
                throw new NoSuchElementException(excptnMsg);
            }
        }

        @Override
        public void remove() {
            if (isNext) {
                currentNode = removeNode(currentNode);
                isNext = false;
            } else {
                throw new IllegalStateException(excptnMsg);
            }
        }
    }
}

