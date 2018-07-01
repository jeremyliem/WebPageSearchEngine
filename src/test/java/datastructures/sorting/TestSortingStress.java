package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;
import org.junit.Test;

import datastructures.interfaces.IPriorityQueue;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=10*SECOND)
    public void testPlaceholder() {
        assertTrue(true);
    }
    
    @Test(timeout=10*SECOND)
    public void topKSortInsert() {
    IList<Integer> list1 = new DoubleLinkedList<Integer>();
    for (int i = 0; i < 500000; i++) {
            list1.add(i);
    }
    IList<Integer> top = Searcher.topKSort(500000, list1);
    assertEquals(500000, top.size());
    }
    

    @Test(timeout=10*SECOND)
    public void testNegativeK() {
    IList<Integer> list1 = new DoubleLinkedList<Integer>();
    for (int i = 0; i < 500000; i++) {
            list1.add(i);
    }
    try {
        IList<Integer> top = Searcher.topKSort(-1, list1);
    // We didn't throw an exception? Fail now.
    fail("Expected IllegalArgumentException");
} catch (IllegalArgumentException ex) {
    // Do nothing: this is ok
}
    }
    
    
    @Test(timeout=10*SECOND)
    public void topInsertReverse() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 499999; i >=0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(500000, list);
        assertEquals(500000, top.size());
    }
    
    
    
    @Test(timeout=10*SECOND)
    public void testInsert() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 500000; i++) {
            heap.insert(i);            
        }
        assertEquals(500000, heap.size());
    }
    
    @Test(timeout=10*SECOND)
    public void testInsertandRemove() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 500000; i++) {
            heap.insert(i);            
        }
        for (int i = 0; i < 500000; i++) {
            heap.removeMin();
        }
        assertEquals(0, heap.size());
    }
    
    @Test(timeout=10*SECOND)
    public void topKsmalle() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> list2 = new ArrayList<Integer>();
        Random rand = new Random("GGCSE373".hashCode());
        for (int i = 0; i < 500000; i++) {
            int random = rand.nextInt((5000000) + 1);
            list.add(random);
            list2.add(random);
        }
        IList<Integer> top = Searcher.topKSort(200, list);
        Collections.sort(list2);
        for (int i = 0; i < 200; i++) {
            assertEquals(top.get(i), list2.get((list2.size() - top.size()) + i));
        }
        
    }
 
}
