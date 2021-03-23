package com.epam.rd.java.basic.practice2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StackImpl implements Stack {

    private Object[] stack;
    private int freeIndex;
    private static String excptnMsg = "No such element exception";

    public StackImpl() {
        stack = new Object[10];
        freeIndex = 0;
    }

    public static void main(String[] args) {

        Stack stack = new StackImpl();

        stack.push("A");
        stack.push("B");
        stack.push("C");
        System.out.println(stack);
        System.out.println(stack.size());

        System.out.println("pop:");
        while(stack.top() != null) {
            System.out.println(stack.pop());
        }
        stack.clear();

        stack.push("A");
        stack.push("B");
        stack.push("C");
        System.out.println(stack);
        Iterator it = stack.iterator();
        System.out.println("Trying to pop without ever using next()");
        try {
            it.remove();
        } catch (IllegalStateException ex) {
            System.out.println(excptnMsg);
        }
        System.out.println(it.next());
        it.remove();
        System.out.println("After removing an element on the top: " + stack);
        while(it.hasNext()) {
            System.out.println(it.next());
            it.remove();
        }
        System.out.println("Trying to pop element after already removing the next one");
        try {
            it.remove();
        } catch (IllegalStateException ex) {
            System.out.println(excptnMsg);
        }

    }

    @Override
    public void push(Object element) {
        if (freeIndex >= stack.length) {
            Object[] newStack = new Object[stack.length * 2];
            for (int i = 0; i < freeIndex; i++) {
                newStack[i] = stack[i];
            }
            stack = newStack;
        }
        stack[freeIndex] = element;
        freeIndex++;
    }

    @Override
    public Object pop() {
        if (freeIndex > 0) {
            freeIndex--;
            Object element = stack[freeIndex];
            stack[freeIndex] = null;
            return element;
        }
        return null;
    }

    @Override
    public Object top() {
        if(freeIndex > 0) {
            return stack[freeIndex - 1];
        }
        return null;
    }

    @Override
    public void clear() {
        stack = new Object[10];
        freeIndex = 0;
    }

    @Override
    public int size() {
        return freeIndex;
    }

    @Override
    public Iterator<Object> iterator() {
        return new IteratorImpl();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < freeIndex - 1; i++) {
            sb.append(stack[i]).append(", ");
        }
        if (freeIndex != 0) {
            sb.append(stack[freeIndex - 1]);
        }
        return sb.append("]").toString();
    }


    private class IteratorImpl implements Iterator {

        private boolean isNext;
        private int currentIndex;

        public IteratorImpl() {
            isNext = false;
            currentIndex = freeIndex;
        }

        @Override
        public boolean hasNext() {
            return (currentIndex > 0);
        }

        @Override
        public Object next() {
            if (hasNext()) {
                currentIndex--;
                isNext = true;
                return stack[currentIndex];
            } else {
                throw new NoSuchElementException(excptnMsg);
            }
        }

        @Override
        public void remove() {
            if (isNext) {
                removeElement(currentIndex);
                isNext = false;
            } else {
                throw new IllegalStateException(excptnMsg);
            }
        }

        private void removeElement(int index) {
            if (index >= 0 && index < freeIndex) {
                if (freeIndex == 1) {
                    pop();
                } else {
                    Object[] newStack = new Object[stack.length - 1];
                    int j = 0;
                    for (int i = 0; i < freeIndex; i++) {
                        if (i == index) {
                            j++;
                        }
                        newStack[i] = stack[i + j];
                    }
                    stack = newStack;
                    freeIndex--;
                }
            }
        }
    }
}
