package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int size;
    private int height;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
        heap = makeArrayOfT(5);
        size = 0;
        height = 1;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int bigsize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[bigsize]);
    }

    @Override
    public T removeMin() {
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
            T min = this.heap[0];
            this.heap[0] = this.heap[this.size -1];
            this.heap[this.size-1] = null;
            this.size--;
            percolateDown();            
            return min;
    }
    
    private void percolateDown() {
    	int cursor = 0;
    	boolean changed = true;
    	while ((4 * cursor +1) <=size -1 && changed) {
    		changed = false;
    		int index = downHelper(cursor);
    		if (heap[cursor].compareTo(heap[4*cursor+index]) > 0) {
    					T swap = heap[cursor];
    					heap[cursor] = heap[4*cursor+index];
    					heap[4*cursor + index] = swap;	
    					cursor = 4*cursor + index;
    					changed = true;
    		}
    	}
    			
    }
    
    private int downHelper(int cursor) {
    	int index = 1;
    	T min = heap[4*cursor + 1];
		for (int i = 2; i<= 4; i++) {
			if ((4 * cursor +i) <= size -1) {
				if (min.compareTo(heap[4*cursor + i])>0) {
					min = heap[4*cursor + i];
					index  = i;
				}
			}
		}
		return index;
    }
    
  
    

    @Override
    public T peekMin() {
        if  (size == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == heap.length-1)  {
            heap = resize(heap);
        } 
            heap[size] = item;
            size++;
            percolateUp();
            if (heap.length<10) {
            	heap.toString();
            } 
     }
    
    
    private void percolateUp() {
    	int cursor = size - 1;
        while (heap[cursor].compareTo(heap[(cursor - 1) / 4]) < 0) {
            int parentIndex = ((cursor - 1) / 4);
            T temp = heap[cursor];
            heap[cursor] = heap[parentIndex];
            heap[parentIndex] = temp;
            cursor = parentIndex;
        }
    }
   
        
    @Override
    public int size() {
        return size;
    }
    
    private T[] resize(T[] old) {
        T[] update = makeArrayOfT(heap.length + (int) Math.pow(NUM_CHILDREN, height+1));
        height++;
        for (int i = 0; i < old.length - 1; i++) {
            update[i] = heap[i];
        }
        return update;
    }
}


