package edu.puc.concurrentavl;

public class RedBlackTree extends BinarySearchTree {
	
	private enum Color {RED, BLACK};
	private Color color = Color.BLACK;
	
	public RedBlackTree() {}
	public RedBlackTree(RedBlackTree parent, int value) {
		this.value = value;
		this.hasValue = true;
		this.parent = parent;
	}
	
	protected void createChildNode(int direction, int newValue)
	{
		children[direction] = new RedBlackTree(this, value);	
	}
	
}
