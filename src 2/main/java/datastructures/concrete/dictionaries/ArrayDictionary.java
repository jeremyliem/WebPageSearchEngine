package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;



public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    private Pair<K, V>[] pairs;

private int size;
    
    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(6);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) {
        for (int i = 0; i < this.size(); i++) {
            if (this.pairs[i].key == key || this.pairs[i].key.equals(key)) {
                return this.pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        int location = this.index(key);
        if (location != -1) {
            this.pairs[location].value = value;
        } else {
            resize();
            this.pairs[this.size] = new Pair<K, V>(key, value);
            this.size++;
        }
    }

    @Override
    public V remove(K key) {
        int location = this.index(key);
        if (location != -1){
            V removedValue = this.pairs[location].value;
            for (int i = location; i < this.size() - 1; i++) {
                this.pairs[i] = this.pairs[i + 1];
            }
            this.size--;
            return removedValue;
        }
        throw new NoSuchKeyException();
    }

    @Override
    public boolean containsKey(K key) {
        return index(key) != -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    // Private Helper Methods
    private int index(K key) {
        for (int i = 0; i < this.size(); i++) {
            if (key != null) {
            if (this.pairs[i].key == key || key.equals(this.pairs[i].key)) {
                return i;
            } 
        } else {
            if (this.pairs[i].key == key || this.pairs[i].equals(key)) {
                return i;
        }
    }
        }
        return -1;
    }

    private void resize() {
        if (this.size() == this.pairs.length) {
            Pair<K, V>[] doubleSize = this.makeArrayOfPairs(2 * this.size());
            for (int i = 0; i < this.size(); i++) {
                doubleSize[i] = this.pairs[i];
            }
            this.pairs = doubleSize;
        }
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
         return new ArrayDictionaryIterator<>(this.pairs, size());
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int index;
        private int size;
        
        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
            this.index = 0;
        }
        
        public boolean hasNext() {
            return this.index < this.size;
        }
        
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                KVPair<K, V> returned = new KVPair<K, V>(this.pairs[index].key, this.pairs[index].value);
                this.index++;
                return returned;
            }
        }
        
    }
    }

