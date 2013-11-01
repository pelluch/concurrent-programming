package edu.puc.concurrentavl;

public class AvlTree implements ISearchTree, ITreeHolder {
	
	protected Node root = null;
	protected int height = 0;
    protected TreeGraph graph;
	@Override
	
	public void updateRoot(Node newRoot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String message, Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean find(int value) {
	      if(root != null)
	            return root.find(value);
	        return false;
	}

	@Override
	public void insert(int newValue) {
		 if(root != null)
			    root.insert(newValue);
	        else
	            root = new Node(null, newValue, this);
	}

	@Override
	public void delete(int delVal) {
		// TODO Auto-generated method stub
		 if(root != null)
			    root.delete(delVal);
		
	}
	
	public void print()
	{
        graph = new TreeGraph(800,600);
        graph.drawTree(root);
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
