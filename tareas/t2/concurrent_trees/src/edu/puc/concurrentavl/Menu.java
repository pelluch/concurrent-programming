package edu.puc.concurrentavl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class Menu {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		RedBlackTree tree = new RedBlackTree();
		tree.insert(5);
		tree.insert(3);
		tree = tree.rotate(tree.RIGHT);
		PrintStream ps = new PrintStream(new File("output"));
		tree.print(ps, 2);
		
		//tree.printOrder(ps);
		ps.close();
		String[] cmd = { "/usr/bin/python", "transposer.py" };
		try {
			Process p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
