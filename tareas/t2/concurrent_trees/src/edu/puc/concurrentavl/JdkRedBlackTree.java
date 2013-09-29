package edu.puc.concurrentavl;

import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: pablo
 * Date: 9/29/13
 * Time: 6:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class JdkRedBlackTree extends TreeMap implements ISearchTree {

    private TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();

    public void insert(int value) {
        treeMap.put(value, value);
    }

    public boolean find(int value) {
        return treeMap.containsKey(value);
    }

    public void delete(int value) {
        treeMap.remove(value);
    }

}
