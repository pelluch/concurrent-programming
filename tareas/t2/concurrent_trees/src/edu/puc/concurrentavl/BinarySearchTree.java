package edu.puc.concurrentavl;
import java.io.PrintStream;

public class BinarySearchTree implements ISearchTree {

	public final int LEFT = 0;
	public final int RIGHT = 1;

	protected BinarySearchTree parent;
	protected int value;
	protected boolean hasValue;
	protected BinarySearchTree[] children = new BinarySearchTree[2];

	public BinarySearchTree() {
		this.hasValue = false;
		this.parent = null;
	}
	public BinarySearchTree(BinarySearchTree parent, int value) {
		this.value = value;
		this.hasValue = true;
		this.parent = parent;
	}
	
	@Override
	public void delete(int delVal) {
		// TODO Auto-generated method stub
		
		int numChildren = numChildren();
		
		//Root node
		if(this.parent == null && this.value == delVal && numChildren == 0)
		{
			this.hasValue = false;
			return;
		}
		
		if(value == delVal)
		{
			
			//System.out.println("Deleting " + value + " with numChildren = " + numChildren);
			if(numChildren == 2)
			{
				BinarySearchTree successor = children[RIGHT].findMin();
				this.value = successor.value;
				successor.delete(successor.value);
			}
			else if(numChildren == 1) 
			{
				
				BinarySearchTree child = children[LEFT] != null ? children[LEFT] : children[RIGHT];
				//Check if root
				if(parent != null)
				{
					int directionFromParent = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
					parent.children[directionFromParent] = child;
				}
				
				child.parent = this.parent;
			}
			else 
			{	
				int directionFromParent = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
				parent.children[directionFromParent] = null;
				this.parent = null;
			}
		}
		
		int direction = delVal < value ? LEFT : RIGHT;
		BinarySearchTree child = children[direction];
		if(child != null)
			child.delete(delVal);
		
		
	}	

	
	@Override
	public boolean find(int value) {	

		int nodeVal = this.value;		
		if(value == nodeVal) 
			return true;	

		else if(children[LEFT] != null && value < nodeVal )
			return children[LEFT].find(value);

		else if(children[RIGHT] != null)
			return children[RIGHT].find(value);

		return false;
	}
	
	protected BinarySearchTree findMin()
	{
		if(children[LEFT] != null)
			return children[LEFT].findMin();
		else
			return this;
	}
	
	@Override
	public void insert(int newValue) {
		// TODO Auto-generated method stub
		
		if(this.parent == null && !hasValue)
		{
			this.value = newValue;
			this.hasValue = true;
			return;
		}
		
		if(this.value == newValue)
			return;
		
		int direction = newValue < value ? LEFT : RIGHT;
		BinarySearchTree child = children[direction];
		if(child == null)
			children[direction] = new BinarySearchTree(this, newValue);
		else
			child.insert(newValue);
	}

	public boolean isValid()
	{
		boolean isValid = true;
		
		if(children[RIGHT] != null)
			isValid = children[RIGHT].value > value && isValid;
		
		if(children[LEFT] != null)
			isValid = children[LEFT].value < value && isValid;
		
		return isValid;
	}
	
	protected int numChildren()
	{
		int numChildren = 0;
		if(children[LEFT] != null)
			++numChildren;
		if(children[RIGHT] != null)
			++numChildren;
		
		return numChildren;
	}

	public void print()
	{
		print(System.out, "", "",  '-');
	}

	public void print(PrintStream os)
	{
		print(os, "", "", '-');
	}
	
	protected void print(PrintStream ps, String whitespace, String prefix,  char padding)
	{			
		
		int numChildren = numChildren();
		if(children[RIGHT] != null) 
		{		
			children[RIGHT].print(ps, whitespace + "          |", "----",  padding);		
		}
		
	
		ps.print(whitespace);
		ps.print(prefix);	
		
		String numString = "" + value;
		while(numChildren > 0 && numString.length() + prefix.length() < 10)
			numString += padding;
		
		ps.println(numString);
		
		if(children[LEFT] != null) 
		{
			children[LEFT].print(ps, whitespace + "          |", "----", padding);
		}
	}
	
	public void printOrder()
	{
		printOrder(System.out);
	}
	
	public void printOrder(PrintStream ps)
	{
		if(children[LEFT] != null)
			children[LEFT].printOrder(ps);
		
		ps.println(value);
		
		if(children[RIGHT] != null)
			children[RIGHT].printOrder(ps);
	}

}
