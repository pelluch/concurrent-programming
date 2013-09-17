package edu.puc.concurrentavl;

public class Node {
	
	private Node parent;
	private Node[] children = new Node[2];
	private int value;
	
	public int getValue() {
		return value;
	}
	public Node(Node parent, int value)
	{
		this.parent = parent;
		this.value = value;
	}
	
	public Node getParent()
	{
		return parent;
	}
	public void setParent(Node parent)
	{
		this.parent = parent;
	}
	
	public void setChild(Node child, int which)
	{
		this.children[which] = child;
	}
	public Node getChild(int which)
	{
		return children[which];
	}
}
