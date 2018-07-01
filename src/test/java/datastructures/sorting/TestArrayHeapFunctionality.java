package datastructures.sorting;

import static org.junit.Assert.assertTrue;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;

import org.junit.Test;
import static org.junit.Assert.fail;

import java.util.Random;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    @Test(timeout=SECOND)
    public void testSizeUpdates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        assertEquals(1, heap.size());
        heap.insert(100);
        assertEquals(2, heap.size());
        heap.insert(1);
        assertEquals(3, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testInsertMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 500; i++) {
            heap.insert(i);  
        }
        assertEquals(0, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertinReverse() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 10; i > 0; i--) {
            heap.insert(i);
        }
        assertEquals(1, heap.removeMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertMultipleSame() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 5; i++) {
            heap.insert(2);
        }
        assertEquals(2, heap.removeMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertandRemoveMultipleSame() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 5; i++) {
            heap.insert(2);
        }
        assertEquals(2, heap.peekMin());
        for (int i = 4; i > 0; i--) {
            heap.removeMin();
        }
        assertEquals(2, heap.peekMin());
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testInsertNegativeValues() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int limit = -500; 
        for (int i = -1; i >= limit; i--) {
            heap.insert(i);
        }
        assertEquals(-500, heap.peekMin());
        assertEquals(500, heap.size());
    }
    
    
    @Test(timeout=SECOND)
    public void testRemoveMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 300; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 250; i++) {
            heap.removeMin();
        }
        
        assertEquals(250, heap.peekMin());
        assertEquals(50, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMultipleNegative() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int limit = -500;
        for (int i = -1; i >= limit; i--) {
            heap.insert(i);
        }
        for (int i = 0; i < 200; i++) {   
            heap.removeMin();
        }
        assertEquals(-300, heap.peekMin());
        assertEquals(300, heap.size());
        
    }
    
    
    @Test(timeout = SECOND)
    public void testIsEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(2);
        assertEquals(2, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testMultiplePeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(2);
        assertEquals(2, heap.peekMin());
        heap.insert(4);
        assertEquals(2, heap.peekMin());
        heap.insert(1);
        assertEquals(1, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertNullException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
        // We didn't throw an exception? Fail now.
        fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
        // Do nothing: this is ok
    }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(40);
        heap.insert(10);
        heap.insert(30);
        heap.removeMin();
        assertEquals(2, heap.size());
        assertEquals(30, heap.peekMin());
        heap.removeMin();
        assertEquals(1, heap.size());
        assertEquals(40, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
                heap.removeMin();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testPeekException() {
    IPriorityQueue<Integer> heap = this.makeInstance();
    try {
        heap.peekMin();
        fail("Expected EmptyContainerException");
    } catch (EmptyContainerException e) {
        // Do nothing: this is ok
    }
    }
    
    @Test(timeout=SECOND)
    public void testRandomInput() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Random rand = new Random();
        for (int i = 0; i<50; i++) {
            int  n = rand.nextInt(50) + 1;
            heap.insert(n);
        }
        heap.insert(100);  
        for (int i =1; i<=50; i++) {
          heap.removeMin();
            assertEquals(51-i, heap.size());
        }
        assertEquals(100, heap.peekMin());
        } 
}

