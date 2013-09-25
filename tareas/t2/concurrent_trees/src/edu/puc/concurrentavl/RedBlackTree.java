package edu.puc.concurrentavl;

import java.io.PrintStream;

public class RedBlackTree implements ISearchTree {
	
	public final int LEFT = 0;
	public final int RIGHT = 1;
	
	protected int color;
	protected final int BLACK = 0;
	protected final int RED = 1;
	
	protected RedBlackTree parent = null;
	protected int value;
	protected boolean hasValue = false;
	protected RedBlackTree[] children = new RedBlackTree[2];
	protected int height = 0;
	protected boolean unbalanced = false;
	protected static boolean unbalanceOccurred = false;
	
	public RedBlackTree() {
		this.color = RED;
	}
	
	public RedBlackTree(RedBlackTree parent, int value) {
		this();
		this.value = value;
		this.hasValue = true;
		this.parent = parent;
		
	}

	public String colorToString() {
		String s = this.color == BLACK ? "B" : "R";
		return s;
	}
	@Override
	public void delete(int delVal) {

		int numChildren = numChildren();

		//Root node
		if(this.parent == null && this.value == delVal && numChildren == 0)
		{
			this.hasValue = false;
			return;
		}

		if(value == delVal)
		{

			
			if(numChildren == 2)
			{
				System.out.println("Deleting " + value + " with numChildren = " + numChildren);
				RedBlackTree successor = children[RIGHT].findMin();
				System.out.println("Successor: " + successor.value);
				this.value = successor.value;
				successor.delete(successor.value);
			}
			else if(numChildren == 1) 
			{

				RedBlackTree child = children[LEFT] != null ? children[LEFT] : children[RIGHT];
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
		RedBlackTree child = children[direction];
		if(child != null)
			child.delete(delVal);


	}	

	public RedBlackTree rotate(int direction) {
		
		int otherDirection = direction ^ 1;
		if(this.children[otherDirection] == null)
			return this;
		
		if(this.parent == null && !this.hasValue)
			return this;
		
		RedBlackTree oldParent = this.parent; 
		RedBlackTree leftRightChild = this.children[otherDirection].children[direction];
		this.parent = children[otherDirection];
		this.parent.parent = oldParent;
		parent.children[direction] = this;
		this.children[otherDirection] = leftRightChild;
		
		if(leftRightChild != null)
			leftRightChild.parent = this;
		
		return this.parent;
		
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

	protected RedBlackTree findMin()
	{
		if(children[LEFT] != null)
			return children[LEFT].findMin();
		else
			return this;
	}

	@Override
	public void insert(int newValue) {

		//Empty tree
		if(this.parent == null && !hasValue)
		{
			this.value = newValue;
			this.hasValue = true;
			this.color = BLACK;
			return;
		}

		if(this.value == newValue)
			return;

		int direction = newValue < value ? LEFT : RIGHT;
		RedBlackTree child = children[direction];
		if(child == null)
		{			
			if (children[direction ^ 1] == null) 
				++ height;
			
			createChildNode(direction, newValue);			
		}
		else
		{
			child.insert(newValue);		
		}
		if(parent == null && unbalanceOccurred)
		{
			System.out.println("");
			System.out.println("");
			this.print();
			unbalanceOccurred = false;
		}
	}

	protected void createChildNode(int direction, int newValue)
	{
		children[direction] = new RedBlackTree(this, newValue);
		
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
		print(System.out, "", "",  '-', RIGHT, 0);
	}
	
	public void print(int extraWidth)
	{
		print(System.out, "", "",  '-', RIGHT, extraWidth);
	}

	public void print(PrintStream ps, int extraWidth)
	{		
		if(children[RIGHT] != null)
			print(ps, "", "", '-', RIGHT, extraWidth);		
		else if(children[LEFT] != null)
			print(ps, "", "", '-', LEFT, extraWidth);	
		else
			ps.println("" + this.value);

	}
	
	public void print(PrintStream ps)
	{		
		if(children[RIGHT] != null)
			print(ps, "", "", '-', RIGHT, 0);	
		else if(children[LEFT] != null)
			print(ps, "", "", '-', LEFT, 0);
		else
			ps.println("" + this.value);

	}

	protected String replaceLastOf(String str, char ch, char newChar)
	{
		int idx = str.lastIndexOf(ch);
		char[] arr = str.toCharArray();
		arr[idx] = newChar;
		return new String(arr);
	}

	protected void print(PrintStream ps, String whitespace, String prefix,  char padding, int lastDirection, int extraWidth)
	{		

		int numChildren = numChildren();
		String newSpace = whitespace;

		if(children[RIGHT] != null) 
		{				
			if(parent != null && lastDirection == RIGHT)
			{
				newSpace = replaceLastOf(whitespace, '|', ' ');
			}

			children[RIGHT].print(ps, newSpace + "          |", "----",  padding, RIGHT, extraWidth);	

			for(int i = 0; i < extraWidth; ++ i)
				ps.println(newSpace + "          |");
		}

		//ps.print(whitespace);
		//ps.print(prefix);	


		String numString = "" + value;
		if(this.unbalanced) numString =  "[" + numString + "]";
		this.unbalanced = false;
		
		while(numChildren > 0 && numString.length() + prefix.length() < 10)
			numString += padding;
		
		
		String line = whitespace + prefix + numString;
		ps.println(line);

		newSpace = whitespace;

		if(children[LEFT] != null) 
		{
			if(parent != null && lastDirection == LEFT)
			{
				newSpace = replaceLastOf(whitespace, '|', ' ');
			}
			for(int i = 0; i < extraWidth; ++ i)
				ps.println(newSpace + "          |");
			children[LEFT].print(ps, newSpace + "          |", "----", padding, LEFT, extraWidth);


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
