package edu.puc.concurrentavl;

public class BinaryTree implements ISearchTree {

	public final int LEFT = 0;
	public final int RIGHT = 1;
	
	private Node root = null;
	
	@Override
	public boolean find(int value) {
		
		return find(root, value);		
	}

	private boolean find(Node root, int value)
	{
		if(root == null)
		{
			return false;
		}
		
		int nodeVal = root.getValue();
		if(value == nodeVal) 
		{
			return true;	
		}				
		else if(value < nodeVal )
		{
			return find(root.getChild(LEFT), value);
		}
		else
		{
			return find(root.getChild(RIGHT), value);
		}
	}
	
	

	
	public void prettyPrint()
	{
		prettyPrint(root, 0);
	}
	
	private void prettyPrint(Node root, int level)
	{
		if(root == null) 
			return;
		
		for(int i = 0; i < level; ++i)
			System.out.print("\t");
		
		System.out.println(root.getValue());
		prettyPrint(root.getChild(LEFT), level + 1);
		prettyPrint(root.getChild(RIGHT), level + 1);
	}
	
	@Override
	public void insert(int value) {
		// TODO Auto-generated method stub
		
		if(root == null)
		{
			this.root = new Node(null, value);
		}
		else		
			insert(root, null, value);
	}
	
	private void insert(Node root, Node parent, int value)
	{
		int nodeVal = root.getValue();
		if(nodeVal == value) return;
			
		int direction;
		direction = value < nodeVal ? LEFT : RIGHT;
		Node childNode = root.getChild(direction);
		if(childNode == null)
		{
			childNode = new Node(root, value);
			root.setChild(childNode, direction);
		}
		else
		{
			insert(childNode, root, value);
		}
	}
	@Override
	public void delete(int value) {
		// TODO Auto-generated method stub
		delete(root, value);
	}
	private void delete(Node root, int value)
	{
		
	}
}
