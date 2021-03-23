package com.epam.rd.java.basic.practice2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class QueueImpl implements Queue {

    private Object[] queueList;
    private int tailIndex;
    private int headIndex;
    private boolean isEmpty;
    private static String excptnMsg = "No such element exception";

    public QueueImpl() {
        this.queueList = new Object[10];
        headIndex = 0;
        tailIndex = 0;
        isEmpty = true;
    }

    public static void main(String[] args) {
        Queue queue = new QueueImpl();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        System.out.println(queue);
        System.out.println(queue.size());
        System.out.print(queue.dequeue());
        System.out.print(queue.dequeue());

        queue.clear();
        System.out.println(queue);
        System.out.println(queue.size());

        Iterator it = queue.iterator();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        System.out.println("Trying to remove an element without ever using next()");
        try {
            it.remove();
        } catch (IllegalStateException ex) {
            System.out.println(excptnMsg);
        }
        while (it.hasNext()) {
            System.out.print(it.next());
            it.remove();
        }

        System.out.println("Trying to remove an element after already removing the next one");
        try {
            it.remove();
        } catch (IllegalStateException ex) {
            System.out.println(excptnMsg);
        }

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        for (Object b : queue) {
            System.out.println(b);
        }
    }

    @Override
    public void clear() {
        queueList = new Object[10];
        tailIndex = 0;
        headIndex = 0;
        isEmpty = true;
    }

    @Override
    public int size() {
        if (isEmpty) {
            return 0;
        }
        return tailIndex - headIndex + 1;
    }

    @Override
    public Iterator<Object> iterator() {
        return new IteratorImpl();
    }

    @Override
    public void enqueue(Object element) {
        if (element != null) {
            if ((tailIndex + 1) >= queueList.length) {
                enlargeQueue();
            }
            if (isEmpty) {
                queueList[headIndex] = element;
                isEmpty = false;
            } else {
                tailIndex++;
                queueList[tailIndex] = element;
            }
        }
    }

    private void enlargeQueue() {
        Object[] newQueue = new Object[queueList.length * 2];
        for (int i = 0; (headIndex + i) < queueList.length; i++) {
            newQueue[i] = queueList[headIndex + i];
        }
        tailIndex -= headIndex;
        headIndex = 0;
        queueList = newQueue;
    }

    @Override
    public Object dequeue() {
        if (!isEmpty) {
            if (headIndex == tailIndex) {
                isEmpty = true;
            }
            headIndex++;
            return queueList[headIndex - 1];
        }
        return null;
    }

    @Override
    public Object top() {
        if (!isEmpty) {
            return queueList[headIndex];
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        if (!isEmpty) {
            str.append(queueList[headIndex]);
            for (int i = headIndex + 1; i <= tailIndex; i++) {
                str.append(", ").append(queueList[i]);
            }
        }
        str.append("]");
        return str.toString();
    }


    private class IteratorImpl implements Iterator {
        private int currentIndex;
        private boolean isNext;

        public IteratorImpl() {
            this.currentIndex = headIndex - 1;
            isNext = false;
        }

        @Override
        public boolean hasNext() {
            return ((currentIndex + 1 <= tailIndex) && !isEmpty);
        }

        @Override
        public Object next() {
            if (hasNext()) {
                currentIndex++;
                isNext = true;
                return queueList[currentIndex];
            } else {
                throw new NoSuchElementException(excptnMsg);
            }
        }

        @Override
        public void remove() {
            if (isNext) {
                removeElement(currentIndex);
                isNext = false;
                currentIndex--;
            } else {
                throw new IllegalStateException(excptnMsg);
            }
        }

        private void removeElement(int index) {
            if (!isEmpty) {
                Object[] newQueue = new Object[queueList.length - 1];
                int j = 0;
                for (int i = 0; i < queueList.length - 1; i++) {
                    if (i == index) {
                        j++;
                    }
                    newQueue[i] = queueList[i + j];
                }
                queueList = newQueue;
                if (headIndex == tailIndex) {
                    isEmpty = true;
                } else {
                    tailIndex--;
                }
            }
        }

    }
}
