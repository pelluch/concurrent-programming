package edu.puc.concurrentavl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
			JdkRedBlackTree tree = new JdkRedBlackTree();;

		
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
			//tree.print();

			//tree.printOrder(ps);
			ps.close();
			//System.out.println("Valid tree: " + tree.isValid());
			String[] cmd = { "/usr/bin/python", "transposer.py" };
			
			try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}


		} catch (FileNotFoundException e) {
			System.out
			.println("No file found at specified path, test aborted.");
		}
		



	}

}
