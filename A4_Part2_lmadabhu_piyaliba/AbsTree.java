// Assignment 4 Part 2 - Starter Code

// Organization of the File:

// 1. class Contract and ContractException subclasses
//
// 2. Class DupTree_Test -- the tester class
//
// 3. Classes AbsTree, Tree, and DupTree
//
// 4. Class AbsTree_Iterator

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

//***************** Contract Checking *********************

class Contract {
	static void Invariant(boolean b) throws Exception {
		if (!b)
			throw new InvariantError();
	}

	static void Requires(boolean b) throws Exception {
		if (!b)
			throw new PreConditionError();
	}

	static void Ensures(boolean b) throws Exception {
		if (!b)
			throw new PostConditionError();
	}
}

class ContractException extends Exception{}

class PreConditionError extends ContractException {}

class PostConditionError extends ContractException {}

class InvariantError extends ContractException {}

// The Tester Class

class DupTree_Test {

	public static void main(String[] args) throws Exception {
		AbsTree tr = new DupTree(100);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(20);
		tr.insert(75);
		tr.insert(20);
		tr.insert(90);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(75);
		tr.insert(90);

		System.out.println("Initial DupTree: ");
		print(tr);

		tr.delete(50);
		tr.delete(125);
		tr.delete(150);
		tr.delete(75);
		tr.delete(90);
		tr.delete(20);
		tr.delete(100);

		System.out.println("DupTree after some delete's: ");
		print(tr);

		tr.delete(50);
		tr.delete(125);
		tr.delete(150);
		tr.delete(75);
		tr.delete(90);

		System.out.println("DupTree before last delete: ");
		print(tr);
		System.out.println("Attempt to delete last value: ");

		tr.delete(20);
	}

	public static void print(AbsTree tr) throws Exception {
		AbsTree_Iterator it = new AbsTree_Iterator(tr);
		while (it.hasNext())
			System.out.print(it.next() + " ");
		System.out.println();
	}
}

// ***************************************************************

// @invariant ordered()

abstract class AbsTree {
	public AbsTree(int n) throws Exception {
		value = n;
		left = null;
		right = null;
		Contract.Invariant(ordered());
	};

	// @ensures member(n)
	public boolean insert(int n) throws Exception {
		Contract.Invariant(ordered());
		boolean b;

		if (value == n)
			b = count_duplicates();
		else if (value < n)
			if (right == null) {
				right = add_node(n);
				b = true;
			} else
				b = right.insert(n);
		else if (left == null) {
			left = add_node(n);
			b = true;
		} else
			b = left.insert(n);

		Contract.Ensures(member(n));
		Contract.Invariant(ordered());
		return b;
	}

	boolean ordered() {
		// fill in missing code
		return (this.left == null || (this.value > this.left.max().value && this.left.ordered()))
				&& (this.right == null || (this.value < this.right.min().value && this.right.ordered()));
	}


	boolean member(int n) {
		// fill in missing code
		return this.value == n ? true : this.value > n ? this.left==null ? 
				false : this.left.member(n) : this.right == null ? 
						false : this.right.member(n);
	}

	public AbsTree_Iterator iterator() throws Exception {
		return new AbsTree_Iterator(this);
	}


	public boolean delete(int n) throws Exception {
		// cannot delete last value in tree
		
		Contract.Invariant(ordered());
		// fill in Contract.Requires(...);
		AbsTree_Iterator tri = new AbsTree_Iterator(this); 
		int counter = 0;
		while(tri.hasNext()) {
			tri.next();
			counter+=1;
		}
		
		AbsTree t = find(n);
		Contract.Requires(counter>1);
		Contract.Requires(t!=null);
		boolean result = false;

		if (t == null) { // n is not in the tree
			result = false;
		}

		else if (t.get_count() > 1) {
			t.set_count(t.get_count() - 1);
			result = true;
		}

		else if (t.left == null && t.right == null) { // n is a leaf value
			if (t != this) {
				case1(t, this);
				result = true;
			} else
				result = false;
		}

		else if (t.left == null || t.right == null) { // t has one subtree only
			if (t != this) { // check whether t is the root of the tree
				case2(t, this);
				result = true;
			} else {
				if (t.right == null)
					case3(t, "left");
				else
					case3(t, "right");
				result = true;
			}
		}
		// t has two subtrees; go with smallest in right subtree of t
		else {
			case3(t, "right");
			result = true;
		}

		Contract.Invariant(ordered());
		// Contract's Ensures condition is in the subclasses

		return result;
	}

	protected void case1(AbsTree t, AbsTree root) { // remove the leaf
		if (t.value > root.value)
			if (root.right == t)
				root.right = null;
			else
				case1(t, root.right);
		else if (root.left == t)
			root.left = null;
		else
			case1(t, root.left);
	}

	protected void case2(AbsTree t, AbsTree root) { // remove internal node
		if (t.value > root.value)
			if (root.right == t)
				if (t.right == null)
					root.right = t.left;
				else
					root.right = t.right;
			else
				case2(t, root.right);
		else if (root.left == t)
			if (t.right == null)
				root.left = t.left;
			else
				root.left = t.right;
		else
			case2(t, root.left);
	}

	protected void case3(AbsTree t, String side) { // replace t.value and t.count
		if (side == "right") {
			AbsTree min_right_t = t.right.min();
			if (min_right_t.left == null && min_right_t.right == null)
				case1(min_right_t, this); // min_right_t is a leaf node
			else
				case2(min_right_t, this); // min_right_t is a non-leaf node
			t.value = min_right_t.value;
			t.set_count(min_right_t.get_count());
		} else {
			AbsTree max_left_t = t.left.max();
			if (max_left_t.left == null && max_left_t.right == null)
				case1(max_left_t, this); // max_left_t is a leaf node
			else
				case2(max_left_t, this); // max_left_t is a non-leaf node
			t.value = max_left_t.value;
			t.set_count(max_left_t.get_count());

		}
	}

	protected AbsTree find(int n) throws Exception {
		AbsTree result;

		Contract.Invariant(ordered());

		if (value == n)
			result = this;
		else if (value < n)
			if (right == null)
				result = null;
			else
				result = right.find(n);
		else if (left == null)
			result = null;
		else
			result = left.find(n);

		Contract.Ensures(result == null || result.value == n);
		Contract.Invariant(ordered());
		return result;
	}

	public AbsTree min() {
		if (left != null)
			return left.min();
		else
			return this;
	}

	public AbsTree max() {
		if (right != null)
			return right.max();
		else
			return this;
	}

	protected abstract AbsTree add_node(int n) throws Exception;

	protected abstract boolean count_duplicates();

	protected abstract int get_count();

	protected abstract void set_count(int v);

	protected int value;
	protected AbsTree left;
	protected AbsTree right;
}

class Tree extends AbsTree {
	public Tree(int n) throws Exception {
		super(n);
	}

	// insert is inherited from AbsTree and 
	// contract appears there
	
	public boolean delete(int n) throws Exception {
		boolean b = super.delete(n);
		Contract.Ensures(!member(n));
		return b;
	}

	protected AbsTree add_node(int n) throws Exception {
		return new Tree(n);
	}

	protected boolean count_duplicates() {
		return false;
	}

	protected int get_count() {
		return 1;
	}

	protected void set_count(int v) {
	}
}

class DupTree extends AbsTree {
	public DupTree(int n) throws Exception {
		super(n);
		count = 1;
		Contract.Invariant(ordered());
	}

	
	public boolean insert(int n) throws Exception {
		if(find(n)!=null)
			old_count = find(n).get_count();
		else
			old_count = 0;
		Contract.Invariant(ordered());

		boolean b = super.insert(n);

		// fill in missing code for Contract.Ensures(...)
		Contract.Ensures(member(n) && find(n).get_count() == old_count + 1 );
		Contract.Invariant(ordered());
		return b;
	}



	public boolean delete(int n) throws Exception {

		int n_count, old_n_count;

		// fill in missing code to set the value of old_n_count
		if(find(n)!=null)
			old_n_count = find(n).get_count();
		else
			old_n_count = 0;
		boolean b = super.delete(n);

		// set the value of n_count
		if(find(n)!=null)
			n_count = find(n).get_count();
		else
			n_count = 0;
		// fill in missing code for Contract.Ensures(...)
		Contract.Ensures((old_n_count==1 && !member(n))||(old_n_count>1 && member(n) && n_count == old_n_count-1 ));
		return b;
	}

	protected AbsTree add_node(int n) throws Exception {
		return new DupTree(n);
	}

	protected boolean count_duplicates() {
		count++;
		return true;
	}

	protected int get_count() {
		return count;
	}

	protected void set_count(int v) {
		count = v;
	}

	protected int count, old_count;

}

//**************************************************************************

class AbsTree_Iterator {

	
	public AbsTree_Iterator(AbsTree root) throws Exception {
		// fill in missing code for Contract.Requires(...);
		Contract.Requires(root.ordered());
		stack_tree_nodes(root);
		// fill in missing code for Contract.Ensures(...);
		Contract.Ensures(stack.peek().value == root.min().value);
	}

	public boolean hasNext() {
		return !stack.isEmpty() || count > 0;
	}

	public int next() throws Exception {
		int old_value = value;
		int new_value = value;
		// fill in missing code for Contract.Requires(...);
		Contract.Requires(hasNext());
		if (count == 0) {
			AbsTree node = stack.pop();
			value = node.value;
			new_value = value;
			count = node.get_count();
			stack_tree_nodes(node.right);
		}
		count--;

		// fill in missing code for Contract.Ensures(...);
		Contract.Ensures(new_value >= old_value && count >= 0);
		return value;
	}


	private void stack_tree_nodes(AbsTree node) throws Exception {

		Contract.Requires(true);

		int old_value = value;
		AbsTree old_node = node;

		if (node != null) {
			stack.push(node);
			while (node.left != null) {
				stack.push(node.left);
				node = node.left;
			}
		}

		// fill in missing code for Contract.Ensures(...)
	Contract.Ensures(stack.empty()||old_value < stack.peek().value && (old_node==null || stack.peek().value == old_node.min().value));
	}

	private Stack<AbsTree> stack = new Stack<AbsTree>();
	private int value;      // the next value to be returned
	private int count = 0;
}
