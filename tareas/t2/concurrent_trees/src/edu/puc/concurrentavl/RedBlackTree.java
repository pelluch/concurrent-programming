package edu.puc.concurrentavl;

import java.io.IOException;
import java.io.PrintStream;

public class RedBlackTree implements ISearchTree, ITreeHolder {

	protected Node root = null;
	protected int height = 0;
    protected TreeGraph graph;

    public RedBlackTree() {
        graph = new TreeGraph(800,600);
    }
	@Override
	public void delete(int delVal) {
        if(root != null)
		    root.delete(delVal);
	}	

	@Override
	public boolean find(int value) {
        if(root != null)
            return root.find(value);
        return false;
	}
	
	protected Node findMin()
	{
        if(root != null)
		    return root.findMin();
        else
            return null;
	}

	@Override
	public void insert(int newValue) {
        if(newValue == 799) {
            int a = 3;
        }
        if(root != null)
		    root.insert(newValue);
        else
            root = new Node(null, newValue, this);
	}

    public void debug(String message, Node node) {

        System.out.println("DEBUG for node " + node.value + "\t" + message);
        print();

    }
    @Override
    public void updateRoot(Node newRoot) {
        this.root = newRoot;
        //this.root.color = Node.BLACK;
    }

	public void print()
	{
        return;
       /* graph.drawTree(root);
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("Error reading input");
            e.printStackTrace();
        }*/
	}


}
