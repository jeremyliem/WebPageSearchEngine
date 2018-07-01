package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testOneUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        IList<Integer> top = Searcher.topKSort(1, list);
        assertEquals(1, top.size());
        assertEquals(1, top.get(0));
    }
    
    @Test(timeout=SECOND)
    public void testKLarger() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(20, list);
        assertEquals(10, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
        }
    
    @Test(timeout=SECOND)
    public void testKSmaller() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(20, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(80+i, top.get(i));
        }
        }
    
    @Test(timeout=SECOND)
    public void testEmptyList() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> top = Searcher.topKSort(100, list);
        assertEquals(0, top.size());
        }
    
    @Test(timeout=SECOND)
    public void testRandom() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(2);
        list.add(7);
        list.add(1);
        list.add(6);
        list.add(9);
        list.add(5);
        IList<Integer> top = Searcher.topKSort(3, list);
        assertEquals(3, top.size());
        assertEquals(6, top.get(0));
        assertEquals(7, top.get(1));
        assertEquals(9, top.get(2));
    }
    
    @Test(timeout=SECOND)
    public void testDuplicates() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 3; i++) {
            list.add(9);
            list.add(6);
            list.add(0);
        }
        IList<Integer> top = Searcher.topKSort(4, list);
        assertEquals(6, top.get(0));
        assertEquals(9, top.get(1));
        assertEquals(9, top.get(2));
        assertEquals(9, top.get(3));
    }
    
    @Test(timeout=SECOND)
    public void testZeroK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 250; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(0, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND) 
    public void testNegativeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        try {
            IList<Integer> top = Searcher.topKSort(-1, list);
        // We didn't throw an exception? Fail now.
        fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
        // Do nothing: this is ok
    }
    }
    
    @Test(timeout=SECOND)
    public void testDuplicateEntries() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 200; i++) {
            list.add(10);
        }
        for (int i = 0; i < 200; i++) {
            list.add(20);
        }
        
        IList<Integer> top = Searcher.topKSort(400, list);
        assertEquals(400, top.size());
        
        for (int i = 0; i < top.size()/2; i++) {
            assertEquals(10, top.get(i));
        }
        for (int i = 200; i<top.size(); i++) {
            assertEquals(20, top.get(i));
        }
    }
    @Test(timeout=1*SECOND)
    public void topKSmall() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> list2 = new ArrayList<Integer>();
        Random rand = new Random("GGCSE373".hashCode());
        for (int i = 0; i < 500; i++) {
            int random = rand.nextInt((500) + 1);
            list.add(random);
            list2.add(random);
        }
        IList<Integer> top = Searcher.topKSort(100, list);
        Collections.sort(list2);
        for (int i = 0; i < 100; i++) {
            assertEquals(top.get(i), list2.get((list2.size() - top.size()) + i));
        }
    }
    @Test(timeout=1*SECOND)
    public void topKAll() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> list2 = new ArrayList<Integer>();
        Random rand = new Random("GGCSE373".hashCode());
        for (int i = 0; i < 500; i++) {
            int random = rand.nextInt((500) + 1);
            list.add(random);
            list2.add(random);
        }
        IList<Integer> top = Searcher.topKSort(500, list);
        Collections.sort(list2);
        for (int i = 0; i < 500; i++) {
            assertEquals(top.get(i), list2.get((list2.size() - top.size()) + i));
        }
    }
}
