package implementations;

import interfaces.AbstractStack;

import java.util.Iterator;

public class Stack<E> implements AbstractStack<E> {
    private Node<E> top;
    private int size;

    private static class Node<E>{
        private E element;
        private Node<E> previous;

        public Node(E value) {
            this.element = value;
        }

    }

    @Override
    public void push(E element) {
        Node<E> newNode = new Node<>(element);
        newNode.previous = top;
        top = newNode;
        this.size++;
    }

    public void ensureNonEmpty(){
        if (top == null){
            throw new IllegalStateException("Stack Null");
        }
    }

    @Override
    public E pop() {
        ensureNonEmpty();
        E element = this.top.element;
        Node<E> temp = this.top.previous;
        this.top.previous = null;
        top = temp;
        this.size--;
        return element;
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return this.top.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        if (top == null){
            return true;
        } else return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = top;
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E element = current.element;
                this.current = this.current.previous;
                return element;
            }
        };

    }

    @Override
    public String toString() {
        int n = this.size;
        Node<E> temp = top;
        do {
            System.out.println(temp.element);
            temp = temp.previous;
        } while (temp != null);
        return "";
    }
}
