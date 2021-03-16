// Assignment 3 Part 1 
//
// Fill in code in class SafeTree.
// 
// In ThreadSafeTree.main(), change the line
//
// Tree tr = new Tree(5000);
//
// to
//
// Tree tr = new SafeTree(5000);
//
// and test your solution.

import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class ThreadSafeTree { 
	
public static void main(String[] args) {
	
	//Tree tr = new Tree(5000);
	SafeTree tr = new SafeTree(5000);
	System.out.println(tr.value);
	List<InsertNums> threads = new ArrayList<InsertNums>();
	
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	threads.add(new InsertNums(tr));
	 
    int N = threads.size();
    
	try {
		System.out.println("Start Parallel Insertions ...");
		for (int i = 0; i < N; i++) {
			threads.get(i).start();
		}
		for (int i = 0; i < N; i++) {
			threads.get(i).join();
		}
		System.out.println("Done Parallel Insertions ...\nResults:");
		tr.print();
	} catch (Exception e) {
	}
}
}

class InsertNums extends Thread {
	SafeTree tr;

	public InsertNums(SafeTree tr) {
		this.tr = tr;
	}

	public void run() {
		Random r = new Random();
		for (int i = 0; i < 3; i++)
			tr.insert(r.nextInt(10000));
		tr = null;
	}
}

class Tree {

	public Tree(int n) {
		value = n;
		left = null;
		right = null;
	}

	public void insert(int n) {
		if (value == n) {
			return;
		}
		if (value < n)
			if (right == null)
				right = new Tree(n);
			else
				right.insert(n);
		else if (left == null)
			left = new Tree(n);
		else
			left.insert(n);
	}

	void print() {
		if (left != null)
			left.print();
		System.out.print(value + " ");
		if (right != null)
			right.print();
	}

	protected int value;
	protected Tree left, right;

}

class SafeTree extends Tree {

	public SafeTree(int n) {
		// fill in code here
		super(n);
		value = n;
	}

	public void insert(int n) {
		// fill in code here
		this.lock();
		if (value == n) {
			return;
		}
		
		if (value < n)
			if (right == null)
				{
				
				right = new SafeTree(n);
				this.unlock();
				}
			else {
				this.unlock();
				right.insert(n);
			}
				
		else if (left == null)
		{
			left = new SafeTree(n);
			this.unlock();
		}
		else {
			this.unlock();
			left.insert(n);
		}
		
		
		
	}

	synchronized void lock() {
		// fill in code here
		while(flag) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		flag = true;
	}

	synchronized void unlock() {
		// fill in code here
		flag = false;
		notify();
	}

	// add fields here
	protected boolean flag = false;
	protected int value;
}
