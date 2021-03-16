// A4 Part 3 - DON'T MODIFY THIS FILE!

import java.util.ArrayList;
import java.util.List;

class MyStack_Aspect_Example {
	public static void main(String[] args) throws Exception {
		MyStack s = new MyStack();
		s.push(100);
		System.out.println(s.pop());
		System.out.println(s.pop());
	}
}

// @invariant size >= 0
class MyStack {
	// @requires true
	// @ensures size == 1 + old_size && top == old_v
	public void push(int v)   {		
		stk.add(0, v);
		size++;
		top = v;	
	}
	
	//@requires size >= 1
	//@ensures  size == old_size - 1 && result == old_top
	public int pop()   {	
		int v = stk.get(0);
		stk.remove(0);
		size--;
		if (size >= 1)
			top = stk.get(0);  
		return v;
	}

	int size = 0; 
	int top;
	List<Integer> stk = new ArrayList<Integer>();
}