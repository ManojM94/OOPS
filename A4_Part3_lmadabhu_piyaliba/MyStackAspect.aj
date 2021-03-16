public aspect MyStackAspect {
	
	// Declare fields
		int length;
		int old_length;
		int top;
		int old_v;
		int old_top;
		int inv_len;
		 
	// Define POINTCUT for push
		pointcut call_push(int v, MyStack s) : 
			call(void MyStack.push(int)) && args(v) && target(s);
		
	// Define BEFORE ADVICE for push
		before(int v, MyStack s) : call_push(v, s) {
			old_length = s.size;
			old_v = v;
			assert(precondition_push());
			assert(invariant(s));
		}
	   
	// Define AFTER ADVICE FOR push	
		after(int v, MyStack s): call_push(v, s) {
			length = s.size;
			top = s.top;
			assert(postcondition_push());
			assert(invariant(s));
		}
		
	// Define POINTCUT for pop
		pointcut call_pop(MyStack s) : 
			call(int MyStack.pop()) && target(s);
		
	// Define BEFORE ADVICE for pop
		before(MyStack s) : call_pop( s) {
			old_length = s.size;
			old_top = s.top;
			assert(precondition_pop());
			assert(invariant(s));
		}
	   
	// Define AFTER ADVICE FOR pop
		after(MyStack s) returning(int result): call_pop(s) {
			length = s.size;
			assert(postcondition_pop(result));
			assert(invariant(s));
		}
		
		boolean invariant(MyStack s) {
			inv_len = s.size;
			if(inv_len >= 0)
				return true;
			return false;
		}
		
		boolean precondition_push() {
			return true;
		}
		
		boolean postcondition_push() {
			if(length == old_length+1 && top == old_v) 
				return true;
			return false;
		}
		
		boolean precondition_pop() {
			if(old_length >= 1)
				return true;
			return false;
		}
		
		boolean postcondition_pop(int result) {
			if(length == old_length-1 && result == old_top) 
				return true;
			return false;
		}
}
