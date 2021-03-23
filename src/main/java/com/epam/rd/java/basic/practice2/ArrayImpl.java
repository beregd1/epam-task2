package com.epam.rd.java.basic.practice2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayImpl implements Array {

    private static String excptnMsg = "No such element exception";
    private Object[] arrayList;
    private int freeIndex;

    public ArrayImpl() {
        arrayList = new Object[10];
        freeIndex = 0;
    }

    public static void main(String[] args) {
        Array arr = new ArrayImpl();
        arr.add("A");
        arr.add("B");
        arr.add("C");
        System.out.println(arr);
        System.out.println(arr.size());
        System.out.println("Changing B to Z:");
        arr.set(1, "Z");
        System.out.println("get(1) => " + arr.get(1));
        System.out.println("indexOf(\"Z\") => " + arr.indexOf("Z"));

        System.out.println("Remove first:");
        arr.remove(0);
        System.out.println("Remove last:");
        arr.remove(1);
        System.out.println(arr);

        arr.clear();
        System.out.println(arr);
        System.out.println(arr.size());

        arr.add("A");
        arr.add("B");
        arr.add("C");
        Iterator it = arr.iterator();
        System.out.println(it.next());
        it.remove();
        System.out.println(it.next());
        it.remove();
        System.out.println(it.next());
        it.remove();
        System.out.println(arr);
        System.out.println("Trying to remove element after already removing the next one");
        try {
            it.remove();
        } catch (IllegalStateException ex) {
            System.out.println(excptnMsg);
        }
        System.out.println("Trying to remove without ever using next()");
        arr = new ArrayImpl();
        arr.add("A");
        arr.add("B");
        arr.add("C");
        it = arr.iterator();
        try {
            it.remove();
        } catch (IllegalStateException ex) {
            System.out.println(excptnMsg);
        }
    }

    @Override
    public void add(Object element) {
        if (element != null) {
            if (freeIndex >= arrayList.length) {
                Object[] newArray = new Object[arrayList.length * 2];
                for (int i = 0; i < freeIndex; i++) {
                    newArray[i] = arrayList[i];
                }
                arrayList = newArray;
            }
            arrayList[freeIndex] = element;
            freeIndex++;
        }
    }

    @Override
    public void set(int index, Object element) {
        if (index < freeIndex) {
            arrayList[index] = element;
        }
    }

    @Override
    public Object get(int index) {
        if (index < freeIndex) {
            return arrayList[index];
        }
        return null;
    }

    @Override
    public int indexOf(Object element) {
        for (int i = 0; i < freeIndex; i++) {
            if (arrayList[i] == element) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void remove(int index) {
        if (index < freeIndex) {
            Object[] newArray = new Object[arrayList.length];
            int j = 0;
            for (int i = 0; i < freeIndex; i++, j++) {
                if (index == i) {
                    j--;
                } else {
                    newArray[j] = arrayList[i];
                }
            }
            arrayList = newArray;
            freeIndex--;
        }
    }

    @Override
    public void clear() {
        arrayList = new Object[10];
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
        StringBuilder result = new StringBuilder("[");
        if (freeIndex > 0) {
            for (int i = 0; i < freeIndex - 1; i++) {
                result.append(arrayList[i]).append(", ");
            }
            result.append(arrayList[freeIndex - 1]);
        }
        return result.append("]").toString();
    }

    private class IteratorImpl implements Iterator {

        private int currentIndex;
        private boolean isNext;

        public IteratorImpl() {
            currentIndex = -1;
            isNext = false;
        }

        @Override
        public boolean hasNext() {
            return (currentIndex + 1 < freeIndex);
        }

        @Override
        public Object next() {
            if (hasNext()) {
                currentIndex++;
                isNext = true;
                return arrayList[currentIndex];
            } else {
                throw new NoSuchElementException(excptnMsg);
            }
        }

        @Override
        public void remove() {
            if (isNext) {
                ArrayImpl.this.remove(currentIndex);
                isNext = false;
                currentIndex--;
            } else {
                throw new IllegalStateException(excptnMsg);
            }
        }
    }
}
