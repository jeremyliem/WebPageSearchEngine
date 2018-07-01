package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int size;
    private int capacity = 16;
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = makeArrayOfChains(capacity);   
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int newSize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[newSize];
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        if (this.chains[index] == null || !this.chains[index].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        
        return this.chains[index].get(key);
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key); 
        if (!this.containsKey(key)){
                if ((size) / capacity > 0.75) {
                    capacity = capacity*2;
                    IDictionary<K, V>[] update = makeArrayOfChains(capacity); 
                    chains = resize(update); 
                    index = hash(key);
                }
                this.chains[index] = (ArrayDictionary<K, V>) chains[index];
                if (this.chains[index] == null) {
                    this.chains[index] = new ArrayDictionary<K, V>();
                }
                size++;
        } 
        this.chains[index].put(key, value);
        chains[index] = this.chains[index];
    }

    @Override
    public V remove(K key) {
        int index = hash(key);
        if (this.chains[index] == null || !this.chains[index].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        V value = this.chains[index].remove(key);
        size--;
        chains[index] = this.chains[index];
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        int index = hash(key);
        if (this.chains[index] == null) {
            return false;
        } else {
            return this.chains[index].containsKey(key);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }
    
    private int hash(K key) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % capacity;
        }
        return index;
    }
    
    private IDictionary<K, V>[] resize(IDictionary<K, V>[] old){
        for (KVPair<K, V> pair : this) {
            int newHash = hash(pair.getKey());
            if (old[newHash] == null) {
                old[newHash] = new ArrayDictionary<K, V>();
            }
            old[newHash].put(pair.getKey(), pair.getValue());
        }
        return old;
    }       
        

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 4. Think about what exactly your *invariants* are. As a 
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int index;
        private Iterator<KVPair<K, V>> iter;
        
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.index = 0;
            if (this.chains[index] != null) {
                this.iter = this.chains[index].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (chains.length == 0) {
                return false;
            }
            else if (iter != null && iter.hasNext()) {
                return true;
            } else if (this.chains.length - 1 == index) {
                return false;
            }
            
            this.index++;
            while (this.chains[index] == null && index < this.chains.length - 1) {
                index++;
            } 
                    if (this.chains[index]!= null) {
                        iter = chains[index].iterator();
                        return true;
                    }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                return this.iter.next();
            }         
    }       
    }
}


        
