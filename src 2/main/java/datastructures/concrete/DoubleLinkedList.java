package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class DoubleLinkedList<T> implements IList<T> {
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (this.front == null && this.back == null) {
            this.front = new Node<T>(item);
            this.back = this.front;
        } else {
            this.back.next = new Node<T>(item);
            this.back.next.prev = this.back;
            this.back = this.back.next;
        }
        this.size++;
    }

    @Override
    public T remove() {
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }  
        T temporary = this.back.data;
        if (this.size() == 1) {
            this.front = null;
            this.back = this.front;
        } else {
            this.back = this.back.prev;
            this.back.next = null;
        }
        this.size--;
        return temporary;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> temp = this.front;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            return temp.data;
        }
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 ||index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        
        Node<T> temp = this.front;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        if (index == 0){
            Node<T> temp2 = new Node<T>(null, item, this.front.next);
            this.front = temp2; 
            if (this.size == 1) {
                this.back = this.front;
            }
        } else if (index == this.size() - 1) {
            this.back = this.back.prev;
            this.back.next = new Node<T>(this.back, item, null);
            this.back = this.back.next;
        } else {
            this.insert(index, item);
            this.delete(index + 1);
        }
        
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            Node<T> temp = new Node<T>(null, item, this.front);
            this.front = temp;
            if (this.size == 0) {
                this.back = temp;
            }
        } else if (index == this.size) {
            Node<T> temp = new Node<T>(this.back, item, null);           
            this.back.next = temp;
            this.back = temp;
        } else if (index > this.size / 2) {
            Node<T> temp = this.back;
            for (int i = this.size() - 1; i > index; i--) {
                temp = temp.prev;
            }
            Node<T> temp2 = new Node<T>(temp.prev, item, temp);
            temp.prev.next = temp2;
            temp.prev = temp2;
        } else {
            Node<T> temp = this.front;
            for (int i = 0; i < index - 1; i++) {
                temp = temp.next;
            }
            Node<T> temp2 = new Node<T>(temp, item, temp.next);
            temp.next.prev = temp2;
            temp.next = temp2;
        }
        this.size++;
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == this.size() - 1) {
            return this.remove();
        }
        T temporary = this.get(index);
        Node<T> temp = this.front;
        if (index == 0) {
            this.front = this.front.next;
            this.front.prev = null;
        } else {
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
        }
        this.size--;
        return temporary;
    }

    @Override
    public int indexOf(T item) {
        Node<T> temporary = this.front;
        for (int i = 0; i < this.size(); i++) {
            if (temporary.data == item || temporary.data.equals(item)) {
                return i;
            }
            temporary = temporary.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return this.indexOf(other) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at; returns 'false'
         * otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the iterator to
         * advance one element forward.
         *
         * @throws NoSuchElementException
         *             if we have reached the end of the iteration and there are no more
         *             elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                T temporary = current.data;
                current = current.next;
                return temporary;
            }
        }
    }
}
