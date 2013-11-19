package org.javersion.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.javersion.util.AbstractSortedTree.Color;
import org.javersion.util.PersistentSortedMap.Node;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PersistentSortedMapTest {
    
    private static final int RANDOM_SEED = new Random().nextInt();

    private static final String DESC = "Random(" + RANDOM_SEED + ")";
    
    @Test
    public void Ascending_Inserts() {
        assertInsert(ascending(300));
    }
    
    @Test
    public void Ascending_Bulk_Insert() {
        assertBulkInsert(ascending(345));
    }
    
    private void assertBulkInsert(List<Integer> ints) {
        Map<Integer, Integer> map = Maps.newHashMapWithExpectedSize(ints.size());
        for (Integer kv : ints) {
            map.put(kv, kv);
        }
        PersistentSortedMap<Integer, Integer> empty = PersistentSortedMap.empty();
        PersistentSortedMap<Integer, Integer> sortedMap = empty.assocAll(map);
        
        assertThat(empty.size(), equalTo(0));
        assertThat(empty.root(), nullValue());
        assertThat(sortedMap.size(), equalTo(map.size()));
        
        for (Integer kv : ints) {
            assertThat(sortedMap.get(kv), equalTo(kv));
        }
        blacksOnPath = null;
        assertRBProperties(sortedMap.root(), 0);
    }
    
    @Test
    public void Ascending_Deletes() {
        assertDelete(ascending(300));
    }

    private List<Integer> ascending(int size) {
        List<Integer> ints = new ArrayList<>(size);
        for (int i=0; i < size; i++) {
            ints.add(i);
        }
        return ints;
    }

    @Test
    public void Descending_Bulk_Insert() {
        assertBulkInsert(descending(300));
    }

    @Test
    public void Descending_Inserts() {
        assertInsert(descending(300));
    }

    private List<Integer> descending(int size) {
        List<Integer> ints = new ArrayList<>(size);
        for (int i=size; i > 0; i--) {
            ints.add(i);
        }
        return ints;
    }

    @Test
    public void Descending_Deletes() {
        assertDelete(descending(300));
    }
    
    @Test
    public void Random_Inserts() {
        try {
            assertInsert(randoms(500));
        } catch (AssertionError e) {
            throw new AssertionError(DESC, e);
        }
    }
    
    @Test
    public void Removes() {
        assertRemoves(randoms(300));
    }
    
    private void assertRemoves(List<Integer> ints) {
        PersistentSortedMap<Integer, Integer> sortedMap = PersistentSortedMap.empty();
        for (Integer kv : ints) {
            sortedMap = sortedMap.assoc(kv, kv);
        }
        PersistentSortedMap<Integer, Integer> afterRemove;
        for (Integer kv : ints) {
            afterRemove = sortedMap.dissoc(kv);
            assertThat(afterRemove.size(), equalTo(sortedMap.size() - 1));
            for (Integer kv2 : ints) {
                if (kv2 == kv) {
                    assertThat(afterRemove.get(kv2), nullValue());
                } else {
                    assertThat(afterRemove.get(kv2), equalTo(kv2));
                }
            }
        }
    }
    
    @Test
    public void Re_Insertions() {
        List<Integer> ints = randoms(10);
        PersistentSortedMap<Integer, Integer> sortedMap = PersistentSortedMap.empty();
        for (int i=0; i < 3; i++) {
            for (Integer kv : ints) {
                sortedMap = sortedMap.assoc(kv, kv);
            }
        }
        assertThat(sortedMap.size(), equalTo(10));
        for (Integer kv : ints) {
            assertThat(sortedMap.get(kv), equalTo(kv));
        }
        blacksOnPath = null;
        assertRBProperties(sortedMap.root(), 0);
    }
    
    @Test
    public void Random_Bulk_Insert() {
        try {
            assertBulkInsert(randoms(300));
        } catch (AssertionError e) {
            throw new AssertionError(DESC, e);
        }
    }
    
    @Test
    public void Random_Deletes() {
        try {
            assertDelete(randoms(500));
        } catch (AssertionError e) {
            throw new AssertionError(DESC + ": " + e.getMessage(), e);
        }
    }

    private List<Integer> randoms(int size) {
        Random random = new Random(RANDOM_SEED);
        Set<Integer> ints = Sets.newLinkedHashSetWithExpectedSize(size);
        for (int i=0; i < size; i++) {
            ints.add(random.nextInt());
        }
        return new ArrayList<>(ints);
    }
    
    @Test
    public void CLR_P269() {
        // Example tree
        assertInsert(
                11,
                2,
                14,
                1,
                7,
                15,
                5,
                8,
                4
                );
        // Same nodes in ascending order
        assertInsert(
                1,
                2,
                4,
                5,
                7,
                8,
                11,
                14,
                15
                );
    }
    
    private void assertInsert(Integer... ints) {
        assertInsert(Arrays.asList(ints));
    }
    
    private void assertInsert(List<Integer> ints) {
        PersistentSortedMap<Integer, Integer> map = PersistentSortedMap.empty();
        List<PersistentSortedMap<Integer, Integer>> maps = new ArrayList<>(ints.size());
        for (Integer i : ints) {
            map = assoc(map, i);
            maps.add(map);
        }

        assertRBMaps(maps, ints);
    }

    private void assertDelete(List<Integer> ints) {
        PersistentSortedMap<Integer, Integer> map = PersistentSortedMap.empty();
        List<PersistentSortedMap<Integer, Integer>> maps = new ArrayList<>(ints.size());
        for (Integer i : ints) {
            map = assoc(map, i);
            maps.add(map);
        }
        for (int i = ints.size() - 1; i > 0; i--) {
            Integer key = ints.get(i);
            map = map.dissoc(key);
            blacksOnPath = null;
            assertRBProperties(map.root(), 0);
            maps.set(i-1, map);
        }
        assertRBMaps(maps, ints);
    }
    
    private void assertRBMaps(
            List<PersistentSortedMap<Integer, Integer>> maps,
            List<Integer> ints) {
        for (int i=0; i < ints.size(); i++) {
            PersistentSortedMap<Integer, Integer> map = maps.get(i);
            blacksOnPath = null;
            assertRBProperties(map.root(), 0);
            assertThat(map.size(), equalTo(i+1));
            for (int j=0; j < ints.size(); j++) {
                Integer key = ints.get(j);
                // Contains all values of previous maps
                if (j <= i) {
                    assertThat(map.get(key), equalTo(key));
                } 
                // But none of the later values
                else {
                    assertThat(map.get(key), nullValue());
                }
            }
        }
    }

    private Integer blacksOnPath = null;
    
    private void assertRBProperties(Node<Integer, Integer> node, int blacks) {
        assertThat(node.color, not(nullValue()));
        if (node.color == Color.RED) {
            assertBlack(node.left);
            assertBlack(node.right);
        } else {
            blacks++;
        }
        boolean leaf = true;
        if (node.left != null){
            assertRBProperties(node.left, blacks);
            leaf = false;
        }
        if (node.right != null) {
            assertRBProperties(node.right, blacks);
            leaf = false;
        }
        if (leaf) {
            if (blacksOnPath == null) {
                blacksOnPath = blacks;
            } else {
                assertThat(blacks, equalTo(blacksOnPath.intValue()));
            }
        }
    }
    
    private void assertBlack(Node<?, ?> node) {
        assertTrue("Expected black node (or null)", node == null || node.color == Color.BLACK);
    }
    
    private PersistentSortedMap<Integer, Integer> assoc(PersistentSortedMap<Integer, Integer> map, Integer i) {
        return map.assoc(i, i);
    }
}
