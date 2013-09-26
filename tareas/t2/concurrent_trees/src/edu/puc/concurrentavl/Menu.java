package edu.puc.concurrentavl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
public class Menu {


	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		int[] values = generateValues();
		RedBlackTree tree = runRedBlack(values);
		JavaRedBlack<Integer, Integer> jrb = runTreeMap(values);
		PrintStream ps;
		try {
			ps = new PrintStream(new File("output"));
			tree.print(ps, 2);
			ps.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		//tree.printOrder(ps);
		
		String[] cmd = { "/usr/bin/python", "transposer.py" };
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			ps = new PrintStream(new File("output"));
			jrb.print(ps, 2);
			ps.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Process p2 = Runtime.getRuntime().exec(cmd);
			p2.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public static RedBlackTree runRedBlack(int [] values) {
		RedBlackTree tree = new RedBlackTree();
		for(int i = 0; i < values.length; ++i) {
			tree.insert(values[i]);
		}
		
		
		
		
		return tree;
	}
	
	public static JavaRedBlack<Integer, Integer> runTreeMap(int [] values) {
		JavaRedBlack<Integer, Integer> map = new JavaRedBlack<Integer, Integer>();
		for(int i = 0; i < values.length; ++i) {
			map.put(values[i], values[i]);
		}
		return map;
		
	}
	
	public static int[] generateValues() {
		int [] values = {1, 2, 3, 4, 5, 6, 7, 800, 799, 801, 802, 740, 10, 8};
		return values;
	}

}

