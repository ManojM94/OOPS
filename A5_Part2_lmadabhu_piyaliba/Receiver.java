import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import Scheduler.Guard;
import Scheduler.Process;
import Scheduler.Scheduler;
class Driver1 {
	
	static String op;
	static int val;
	
	public static void main(String[] args) {

		
		Producer1 p1 = new Producer1();
		Producer2 p2 = new Producer2();
		Producer3 p3 = new Producer3();
		Receiver r = new Receiver();
		Process[] arr = {p1,p2,p3,r};
		Scheduler s = new Scheduler(arr, false);
				// true => show the scheduler's log
				// false => don't shown any log
		s.start();
		 
	}
}
class Producer1 extends Process   {    

	public void run() {
		for (int v = 1; v < 51; v++) {
			send_data("c1", v);
		}
	}
	}

class Producer2 extends Process   {    
	
	public void run() {
		for (int v = 1; v < 51; v++) {
			send_data("c2", v);
		}
	}	
}
class Producer3 extends Process   {    
	
	public void run() {
		for (int v = 1; v < 51; v++) {
			send_data("c3", v);
		}
	}	
}


class Receiver extends Process {

  class G implements Guard {
     public boolean test(Object o) {
		 
	// Write Java code using is_waiting 
        // and channels "c1", "c2" and "c3"
	
	return is_waiting("send","c1")||is_waiting("send","c2")||is_waiting("send","c3");
	 
    }
  }
  
  public void run() {	
	
     Guard g = new G();
     while (true) {
			 
        // Write Java code using await, is_waiting, receive,  
        // and channels "c1", "c2" and "c3"
	await(g);
	if(is_waiting("send", "c1")||is_waiting("send","c2"))
		receive("c1","c2");
	else{
		receive("c3");
	}

        int v = (int) get_data();
        System.out.println(v);
    }
  }

}
