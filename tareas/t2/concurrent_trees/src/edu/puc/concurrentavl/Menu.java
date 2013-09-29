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
		
		
		PrintStream ps;
		try {
			RedBlackTree tree = runRedBlack(values);

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
	
	public static RedBlackTree runRedBlack(int [] values) throws FileNotFoundException {




        RedBlackTree tree = new RedBlackTree();
		for(int i = 0; i < values.length; ++i) {

            File f = new File("output");
            if(f.exists()) {
                f.delete();
            }

            PrintStream ps = System.out;
            String op = "";
            if(values[i] < 0)     {
                op = "DELETE " + -values[i];
                ps.println(op);
                tree.delete(-values[i]);
                tree.print();

            }
            else {
                op = "INSERT " + values[i];
                ps.println(op);
			    tree.insert(values[i]);
            }


//            String[] cmd = { "/usr/bin/python", "transposer.py", op };
//
//            try {
//                Process p = Runtime.getRuntime().exec(cmd);
//                try {
//                    p.waitFor();
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            //ps.close();
		}

		return tree;
	}

	
	public static int[] generateValues() {
		int [] values = {5, 2, 9, 10, 11, 12, 1, 3, 4, -9, -10, -12, -2, -5, -11, -4, -3, -1};
		return values;
	}

}

