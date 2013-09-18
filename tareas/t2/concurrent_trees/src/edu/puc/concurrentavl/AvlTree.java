package edu.puc.concurrentavl;

public class AvlTree extends BinarySearchTree {

	protected int balance = 0;
	
	public void restoreBalance()
	{
		int balance = 0;
		if(children[RIGHT] != null)
			balance += children[RIGHT].getHeight();
		if(children[LEFT] != null)
			balance -= children[LEFT].getHeight();		
		
		
		
	}
	public void insert(int newValue)
	{

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
		{
			children[direction] = new BinarySearchTree(this, newValue);
			restoreBalance();	
		}
		else
		{
			child.insert(newValue);
		}
				
	}
	
	public void delete(int delVal)
	{
		super.delete(delVal);
		restoreBalance();		
	}
	
	public void rotate(int direction)
	{
		
	}
	
}
