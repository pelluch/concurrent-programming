package edu.puc.concurrentavl;

import java.io.PrintStream;

public class Node {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	private ITreeHolder owner;
	private Node parent = null;
	private Node[] children = new Node[2];

	int value;

	Node(Node parent, int value, ITreeHolder owner) {
		this.parent = parent;
		this.value = value;
        this.owner = owner;
	}


    public Node getChild(int direction) {
        return children[direction];
    }
    public String directionString(int direction) {
        if(direction == LEFT)
            return "Left";
        else
            return "Right";
    }


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

    public Node findMin() {
        if(children[LEFT] != null)
            return children[LEFT].findMin();
        else
            return this;
    }

    public Node findMax() {
        if(children[RIGHT] != null)
            return children[RIGHT].findMax();
        else
            return this;
    }
    public Node getUncle() {

        Node uncle = null;
        if(parent.parent != null) {

            int uncleDirection = parent.equals(parent.parent.children[RIGHT]) ? LEFT : RIGHT;
            uncle = parent.parent.children[uncleDirection];
        }

        return uncle;
    }

    public Node switchPositionsWithChild(int childDirection) {

        Node child = children[childDirection];
        if(child == null) {
            return null;
        }
        Node oldParent = this.parent;
        if(oldParent != null) {
            int direction = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
            parent.children[direction] = child;   //Update parent's child
        }
        else {
            owner.updateRoot(child);
        }

        this.parent = child; //My child is now my parent
        child.parent = oldParent; //My child's parent is my old parent
        this.children = child.children; //Inherit my child's children
        child.children = new Node[2]; //Their child is now only me
        child.children[childDirection] = this;
        return child;
    }

    public void delete(int delVal) {

        int numChildren = numChildren();

        if(value == delVal)
        {
            if(numChildren == 2)
            {
               // System.out.println("Deleting " + value + " with numChildren = " + numChildren);
                Node successor = children[RIGHT].findMin();
                int oldVal = value;
                this.value = successor.value;
                successor.value = oldVal;
                successor.delete(delVal);
            }
            else if(numChildren == 1)
            {            	
            	int childDirection = children[0] == null ? RIGHT : LEFT;
            	int oldVal = value;
            	this.value = children[childDirection].value;
            	this.children[childDirection].value = oldVal;
            	children[childDirection].delete(delVal);
            }
            else
            {
            	int parentDirection = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
            	parent.children[parentDirection] = null;
            	this.parent = null;
            }
        }
        else {
            int direction = delVal < value ? LEFT : RIGHT;
            Node child = children[direction];
            if(child != null)
                child.delete(delVal);
        }
       //// System.out.println("Done deleting");

    }

    protected String replaceLastOf(String str, char ch, char newChar) {
        int idx = str.lastIndexOf(ch);
        char[] arr = str.toCharArray();
        arr[idx] = newChar;
        return new String(arr);
    }

    public void insert(int newValue) {
        //Value already exists, do nothing
        if(this.value == newValue)
            return;

        int direction = newValue < value ? LEFT : RIGHT;
        Node child = children[direction];
        if(child == null) {
            children[direction] = new Node(this, newValue, owner);
        }
        else {
            child.insert(newValue);
        }
    }

    public Node getBrother() {
        if(this.parent == null) {
            return null;
        }
        Node brother = this.equals(parent.children[LEFT]) ? parent.children[RIGHT] : parent.children[LEFT];
        return brother;
    }


    public void rotate(int direction) {

        int otherDirection = direction ^ 1;
        assert(children[otherDirection] != null);

        Node oldParent = this.parent;
        Node crossChild = this.children[otherDirection].children[direction];

        //children[otherDirection] will be my parent, so it becomes root
        if(parent == null) {
            owner.updateRoot(children[otherDirection]);
        }
        //My parent is the child in otherDirection
        parent = children[otherDirection];
        //His parent is now my old parent
        parent.parent = oldParent;

        if(oldParent != null) {
            if(this.equals(oldParent.children[otherDirection]))
                oldParent.children[otherDirection] = parent;
            else
                oldParent.children[direction] = parent;
        }

        //...and I am now his child
        parent.children[direction] = this;
        children[otherDirection] = crossChild;

        if(crossChild != null)
            crossChild.parent = this;

        return;

    }
    
    public int numChildren()
    {
        int numChildren = 0;
        if(children[LEFT] != null)
            ++numChildren;
        if(children[RIGHT] != null)
            ++numChildren;

        return numChildren;
    }


}

