// A5 Part 2 Java Implementation Outline.
// Your code to be added near end of file

import java.util.*;

import Scheduler.Scheduler;
import Scheduler.Process;
import Scheduler.Guard;

class Driver {
	
	static String op;
	static int val;
	
	public static void main(String[] args) {
		Producer p = new Producer();
		Consumer c  = new Consumer();
		Circular_Buffer b = new Circular_Buffer();
		
		Process[] arr = {p, c, b};
		Scheduler s = new Scheduler(arr, false);
				// true => show the scheduler's log
				// false => don't shown any log
		s.start();
		 
	}
}

class Producer extends Process   {    

	public void run() {
		for (int v = 1; v < 51; v++) {
			send_data("put", v);
		}
	}
	}

class Consumer extends Process   {    
	
	public void run() {
		while (true) {
			try { Thread.sleep(new Random().nextInt(100)); } 
			catch (Exception e) {}
			receive("get");
			//Not needed: int v = (int) get_data();
		}
	}	
}

class Circular_Buffer extends Process  {
	
	private final int n = 5;
	private int[] data = new int[n];
	private int p = 0;
	private int g = 0;
	private int count = 0;
	
	private void put(int v) {
		data[p] = v;
		p = (p + 1) % n;
		count++;
		
		System.out.println("Put " + v);
		Driver.op = "Put";
		Driver.val = v;
	}
	
	private int get() {
		int v = data[g];
		g = (g + 1) % n;
		count--;
		
		Driver.op = "Get";
		Driver.val = v;
		System.out.println("Get " + v);
		return v;
	}
	
	public void run() {	
		while (true) {
		// write your code here
			if(count==0) {
				receive("put");
				chosen="put";
			}
			else if(count==n) {
				send("get");
				chosen="get";
			}
			else {
				send_or_receive("get", "put");
			}
			switch(chosen) {
			case ("put"):
					put((int) get_data());
					break;
			case ("get"):
					send_data("get",get());
					break;
			}
	 
		}
	}
}

