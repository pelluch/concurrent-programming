package edu.puc.concurrentavl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;


public class Tester 
{

	public static void main(String[] args) {
		
		System.out.println("Choose test file: ");
		String basePath = "../tests";
		File dirFile = new File(basePath);
		File[] testFiles = dirFile.listFiles();
		for(int i = 0; i < testFiles.length; ++i) {
			System.out.println((i+1) + ". " + testFiles[i].getName());
		}
		Scanner scanner = new Scanner(System.in);
		
		int idx = -1;
		while(idx < 0 || idx >= testFiles.length)
		{			
			System.out.println("Insert a number in range.");
			try 
			{
				String choice = scanner.nextLine();
				idx = Integer.parseInt(choice)-1;
			}
			catch(NumberFormatException nfe)  
			{  
				System.out.println("Not a number.");				
			}
		}
		
		
		String path = testFiles[idx].getPath();
		
		idx = -1;
		System.out.println("1. Binary Search Tree (unbalanced)");
		System.out.println("2. AVL Tree");
		System.out.println("3. Red-Black Tree");
		
		while(idx < 0 || idx > 2)
		{			
			System.out.println("Choose a BST implementation");
			try 
			{
				String choice = scanner.nextLine();
				idx = Integer.parseInt(choice) - 1;
			}
			catch(NumberFormatException nfe)  
			{  
				System.out.println("Not a number.");				
			}
		}
	
		scanner.close();


		File file = new File(path);
		try {
			scanner = new Scanner(file);
			int numberOfThreads = Integer.parseInt(scanner.nextLine());

			List<Queue<Command>> commandQueues = new ArrayList<Queue<Command>>();
			for (int i = 0; i < numberOfThreads; i++) {
				commandQueues.add(new LinkedList<Command>());
			}

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				Command c = new Command(line);
				commandQueues.get(c.getThread()).add(c);
			}
			
			

			
			long time = System.currentTimeMillis();
			BinarySearchTree tree = null;

			switch(idx)
			{
			case 0:
				tree = new BinarySearchTree();
				break;
			case 1:
				tree = new AvlTree();
				break;
			case 2:
				tree = new RedBlackTree();
				break;
				
			}
			 // Reemplazar con la
			// implementacion del alumno

			TestThread[] threads = new TestThread[numberOfThreads];
			for (int i = 0; i < numberOfThreads; i++) {
				threads[i] = new TestThread(tree, commandQueues.get(i));
				threads[i].start();
			}

			for (int i = 0; i < numberOfThreads; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			System.out.println("Test Passed");
			long delta = System.currentTimeMillis() - time;
			System.out.println("The test took " + delta + " ms.");
			PrintStream ps = new PrintStream(new File("output"));
			tree.print(ps);
			
			//tree.printOrder(ps);
			ps.close();
			System.out.println("Valid tree: " + tree.isValid());



		} catch (FileNotFoundException e) {
			System.out
			.println("No file found at specified path, test aborted.");
		}
		



	}

}
