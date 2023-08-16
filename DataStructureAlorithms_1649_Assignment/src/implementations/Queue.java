package implementations;

import interfaces.AbstractQueue;

import java.util.Iterator;

public class Queue<E> implements AbstractQueue<E> {
    private Node<E> head;
    private int size;

    private static class Node<E>{
        private E element;
        private Node<E> next;

        public Node(E value) {
            this.element = value;
        }

    }

    @Override
    public void offer(E element) {
        Node<E> newNode = new Node<>(element);
        if (this.head == null){
            this.head = newNode;
        } else {
            Node<E> current = this.head;
            while (current.next != null){
                current = current.next;
            }
            current.next = newNode;
        }
        this.size++;
    }

    private void ensureNonEmpty(){
        if (this.head == null){
            throw new IllegalStateException("Queue Null");
        }
    }

    @Override
    public E poll() {
        ensureNonEmpty();
        E element = this.head.element;
        if (this.size == 1) {
            this.head = null;
        } else {
            Node<E> next = this.head.next;
            this.head.next = null;
            this.head = next;
        }
        this.size--;
        return element;
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return this.head.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        if (this.head == null) {
            return true;
        } else return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> temp = head;
            @Override
            public boolean hasNext() {
                return (temp != null);
            }

            @Override
            public E next() {
                E element = temp.element;
                temp = temp.next;
                return element;
            }
        };
    }

}