package edu.puc.concurrentavl;

import java.io.PrintStream;

public interface IPrintable {

	public void print();
	public void print(int extraWidth);
	public void print(PrintStream ps);
	void print(PrintStream ps, int extraWidth);
	void print(PrintStream ps, String whitespace, String prefix,  char padding, int lastDirection, int extraWidth);
	
}
