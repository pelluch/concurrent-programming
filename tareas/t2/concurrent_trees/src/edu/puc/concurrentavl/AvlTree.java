package edu.puc.concurrentavl;

public class AvlTree extends BinarySearchTree {

	protected int balance = 0;
	
	public AvlTree() 
	{
		this.hasValue = false;
		this.parent = null;
	}
	
	public AvlTree(AvlTree parent, int value)
	{
		this.value = value;
		this.hasValue = true;
		this.parent = null;
	}
	public void reBalance()
	{
		
	}
	
	protected void createNode(int direction, int value)
	{
		children[direction] = new AvlTree(this, value);
		AvlTree avlParent = (AvlTree)parent;
	}
	

	
}
