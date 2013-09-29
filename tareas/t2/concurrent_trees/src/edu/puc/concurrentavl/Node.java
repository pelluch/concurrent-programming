package edu.puc.concurrentavl;

import java.io.PrintStream;

public class Node {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int BLACK = 0;
	public static final int RED = 1;

	protected int color = RED;
	private ITreeHolder owner;
	private Node parent = null;
	private Node[] children = new Node[2];

	int value;

	Node(Node parent, int value, ITreeHolder owner) {
		this.parent = parent;
        if(this.parent == null) this.color = BLACK;
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
                System.out.println("Deleting " + value + " with numChildren = " + numChildren);
                Node successor = children[LEFT].findMax();
                int oldVal = value;
                this.value = successor.value;
                successor.value = oldVal;
                successor.delete(delVal);
            }
            else
            {
               //owner.debug("I have one child. Replacing places in tree...", this);
                int childDirection = children[LEFT] != null ? LEFT : RIGHT;
                Node child = switchPositionsWithChild(childDirection);

                //owner.debug("Done");
                if(color == BLACK) {
                       if(child != null && child.color == RED) {
                          //owner.debug("Switching child color to black.", this);
                           child.color = BLACK;
                           child.children[childDirection] = null;

                       }
                        else if(child == null || child.color == BLACK) {
                           //owner.debug("Calling method on child to preserve balance...", this);
                           if(child == null)
                               deleteCaseOne(delVal);
                            else
                               child.deleteCaseOne(delVal);
                       }
                }
                if(parent != null) {
                    int direction = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
                    int otherDirection = direction ^ 1;
                    parent.children[direction] = null;
                }
                else {
                    owner.updateRoot(null);
                }
            }
        }
        else {
            int direction = delVal < value ? LEFT : RIGHT;
            Node child = children[direction];
            if(child != null)
                child.delete(delVal);
        }
       // System.out.println("Done deleting");

    }


    public void deleteCaseOne(int value) {

        System.out.println("Entering case 1");
        //CASE 1 WIKIPEDIA. New root.
        if(parent == null) {
           //owner.debug("Root has changed, end deletion", this);
            owner.updateRoot(this);
        }
        else {
           //owner.debug("Root has not changed, entering case 2", this);
             deleteCaseTwo(value);
        }

    }

    public void deleteCaseTwo(int value) {
        System.out.println("Entering case 2");
        Node brother = getBrother();
       //owner.debug("Checking if brother is red", this);
        if(brother.color == RED) {
            int direction = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
            parent.switchColors(brother);
            parent.rotate(direction);
           //owner.debug("Sibling is red. Switching sibling's color with parent and rotating", this);
        }
        deleteCaseThree(value);
    }

    public void deleteCaseThree(int value) {
        System.out.println("Entering case 3");
        Node brother = getBrother();
        if(parent.color == BLACK && brother.childrenBlack()) {
            brother.color = RED;
            System.out.println("Parent's and brothers children are ALL BLACK.");
           //owner.debug("Going back to case one", this);
            parent.deleteCaseOne(value);
        }
        else {
           //owner.debug("Going to case 4", this);
              deleteCaseFour(value);
        }
    }

    public void deleteCaseFour(int value) {
        System.out.println("Entering case 4") ;
        Node brother = getBrother();
        if(brother.color == BLACK && brother.childrenBlack() && parent.color == RED) {
            System.out.println("Parent's color is RED.");
            parent.switchColors(brother);
           //owner.debug("Switching colors between P and S", this);
        }
        else {
            //owner.debug("Sibling is red. Switching sibling's color with parent and rotating");
            deleteCaseFive(value);
        }
    }

    public void deleteCaseFive(int value) {
        System.out.println("Entering case 5");
        Node brother = getBrother();
        int direction = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
        int otherDirection = direction ^ 1;

        System.out.println("Brother's color: " + brother.colorToString());

        if(brother.color == BLACK && !isBlackOrLeaf(brother.children[direction]) &&
                isBlackOrLeaf(brother.children[otherDirection])) {
            System.out.println("ENTERED IF IN CASE 5");
            brother.rotate(otherDirection);
            brother.switchColors(brother.parent);
        }
       //owner.debug("Going to case 6", this);
        deleteCaseSix(value);
    }

    public void deleteCaseSix(int value) {
        System.out.println("Entering case 6");

        Node brother = getBrother();
        int direction = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
        int otherDirection = direction ^ 1;

        System.out.println("Direction from parent: " + directionString(direction));
       //owner.debug("Brother's color: " + brother.colorToString(), this);

        brother.color = parent.color;
        parent.color = BLACK;

        if(brother.children[otherDirection] != null)
            brother.children[otherDirection].color = BLACK;

       //owner.debug("Finally: " + brother.colorToString(), this);
        parent.rotate(direction);
       //owner.debug("Exiting case 6", this);
    }

    public boolean isBlackOrLeaf(Node node) {
        if(node == null || node.color == BLACK) {
            return true;
        }
        else {
            return false;
        }
    }


    public boolean childrenBlack() {
        if(children[LEFT] != null && children[LEFT].color == RED)
            return false;
        else if(children[RIGHT] != null && children[RIGHT].color == RED) {
            return false;
        }
        else {
            return true;
        }
    }
    public String colorToString() {
		String s = this.color == BLACK ? "B" : "R";
		return s;
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
            children[direction].balanceTree();
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

    public void switchColors(Node other) {
        if(other == null) {
            return;
        }
        else {
            int otherColor = other.color;
            other.color = color;
            color = otherColor;
        }
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

    //Direction: Position inserted FROM parent
    public void balanceTree() {
        //If parent is RED, parent.parent is never null or parent would be BLACK
        if(parent == null) {
            color = BLACK;
        }
        else {
            int direction = this.equals(parent.children[LEFT]) ? LEFT : RIGHT;
            int otherDirection = direction ^ 1;
            Node uncle = getUncle();
            int uncleColor = uncle == null ? BLACK : uncle.color;

            if(parent.color == BLACK) {
                //owner.debug("Parent is black, nothing to do.");
                return;
            }
            else if(parent.color == RED && uncleColor == RED) {
                //owner.debug("Parent is red and so is uncle Changing colors....");
                parent.color = BLACK;
                uncle.color = BLACK;
                parent.parent.color = RED;
                //owner.debug("Rebalancing grandparent.");
                parent.parent.balanceTree();
            }
            else if(parent.color == RED && uncleColor == BLACK &&
                    parent.equals(parent.parent.children[otherDirection])) {

                //owner.debug("Parent is red and uncle is black in other ditection. Rotating parent.");
                parent.rotate(otherDirection);
                //owner.debug("Recursive call..");
                children[otherDirection].balanceTree();
            }
            else if(parent.color == RED && uncleColor == BLACK &&
                    parent.equals(parent.parent.children[direction])) {

                //owner.debug("Parent is red and uncle is black. in same direction. Switching colors...");
                parent.parent.switchColors(parent);
                //owner.debug("Rotating....");
                parent.parent.rotate(otherDirection);
                //owner.debug("Done.");
            }
        }
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

