package cn.cy.base.core.sendqueue;

import java.util.Collection;
import java.util.Iterator;

public class SendQueueSimpleImpl<T> implements SendQueue<T> {

    @Override
    public int maxSize() {
        return 0;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean offer(T t) {
        return false;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public Iterator<T> iterator() {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public Object[] toArray() {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public boolean remove(Object o) {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new IllegalArgumentException("lazy to implement");
    }

    @Override
    public void clear() {

    }
}
