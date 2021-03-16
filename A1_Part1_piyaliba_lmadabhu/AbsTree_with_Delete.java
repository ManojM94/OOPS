// Assignment 1 Part 1: Starter Code

class Tree_Test {

	public static void main(String[] args) {
		AbsTree tr = new Tree(100);
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
		
		tr.delete(20);
		tr.delete(20);
		tr.delete(20);
		tr.delete(150);
		tr.delete(100);
		tr.delete(150);
		tr.delete(125);
		tr.delete(125);
		tr.delete(50);
		tr.delete(50);
		tr.delete(50);
		tr.delete(75);
		tr.delete(90);
		tr.delete(75);
		tr.delete(90);
	}
}

class DupTree_Test {

	public static void main(String[] args) {
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
		
		tr.delete(20);
		tr.delete(20);
		tr.delete(20);
		tr.delete(150);
		tr.delete(100);
		tr.delete(150);
		tr.delete(125);
		tr.delete(125);
		tr.delete(50);
		tr.delete(50);
		tr.delete(50);
		tr.delete(75);
		tr.delete(90);
		tr.delete(75);
		tr.delete(90);
	}
}

abstract class AbsTree {
	public AbsTree(int n) {
		value = n;
		left = null;
		right = null;
	}

	public void insert(int n) {
		if (value == n)
			count_duplicates();
		else if (value < n)
			if (right == null) {
				
				right = add_node(n);
				right.parent = this;
			} else
				right.insert(n);
		else if (left == null) {
			left = add_node(n);
			left.parent = this;
		} else
			left.insert(n);
	}

	public void delete(int n) {  
		AbsTree t = find(n);

		if (t == null) { // n is not in the tree
			System.out.println("Unable to delete " + n + " -- not in the tree!");
			return;
		}

		int c = t.get_count();
		if (c > 1) {
			t.set_count(c-1);
			return;
		}

		if (t.left == null && t.right == null) { // n is a leaf value
			if (t != this) {
				case1(t);
				t.parent = null;
			}
			else
				System.out.println("Unable to delete " + n + " -- tree will become empty!");
			return;
		}
		if (t.left == null || t.right == null) { // t has one subtree only
			if (t != this) { // check whether t is the root of the tree
				case2(t);
				t.parent = null;
				return;
			} else {
				if (t.right == null)
					case3L(t);
				else
					case3R(t);
				t.parent = null;
				return;
			}
		}
		// t has two subtrees; go with smallest in right subtree of t
		case3R(t);
		t.parent = null;
	}

	protected void case1(AbsTree t) { // remove the leaf
		// to be filled by you
		AbsTree par = t.parent;
		if(par.left==t) {
			par.left = null;
		}
		else if(par.right==t) {
			par.right = null;
		}
		t.set_count(0);
	}

	protected void case2(AbsTree t) { // remove internal node
		// to be filled by you
		AbsTree par = t.parent;
		if(par.left==t) {
			par.left = (t.left!=null)?t.left:t.right;
			par.left.parent = par;
		}
		else if(par.right==t) {
			par.right = (t.left!=null)?t.left:t.right;
			par.right.parent = par;
		}
		t.left = t.right = null;
		t.set_count(0);
	}

	protected void case3L(AbsTree t) { // replace t.value and t.count
		// to be filled by you
		AbsTree tmp = t.left.max();
		int current = t.value;
		t.value = tmp.value;
		t.set_count(tmp.get_count());
		tmp.set_count(0);
		
		if(t.left!=tmp) {
			tmp.parent.right = null;
		} else {
			tmp.parent.left = tmp.left;
			if (tmp.left != null) {
				tmp.left.parent = tmp.parent;
			}
		}
		tmp.parent =tmp.left=tmp.right= null;
		tmp.value = current;
	}

	protected void case3R(AbsTree t) { // replace t.value
		// to be filled by you
		AbsTree tmp = t.right.min();
		int current = t.value;
		t.value = tmp.value;
		t.set_count(tmp.get_count());
		tmp.set_count(0);
		
		
		if(t.right!=tmp) {
			tmp.parent.left = null;
		} else {
			tmp.parent.right = tmp.right;
			if (tmp.right != null) {
				tmp.right.parent = tmp.parent;
			}
			
		}
		tmp.parent = tmp.left = tmp.right = null;
		tmp.value = current;
	}

	
	  private AbsTree find(int n) {
		  if (this.value == n)
				return this;
			else if (this.value < n)
			{
				if (right != null) {
					return right.find(n);}
			}
			else if (this.left != null) {
				
				return left.find(n);
			}
				return null;
		  }
	  
	  
	  public AbsTree min() { // to be filled by you
		  if (left != null)
				return left.min();
			else
				return this;
		  }
	  
	  
	  public AbsTree max() { // to be filled by you
		  if (right != null)
				return right.max();
			else
				return this;	
	  }
	 

	protected int value;
	protected AbsTree left;
	protected AbsTree right;
	protected AbsTree parent;

	protected abstract AbsTree add_node(int n);
	protected abstract void count_duplicates();
	protected abstract int get_count();
	protected abstract void set_count(int v);
}

class Tree extends AbsTree {
	public Tree(int n) {
		super(n);
	}

	protected AbsTree add_node(int n) {
		return new Tree(n);
	}

	protected void count_duplicates() {
		;
	}

	protected int get_count() {
		// to be filled by you
		return 0;
	}

	protected void set_count(int v) {
		// to be filled by you
	}


}

class DupTree extends AbsTree {
	public DupTree(int n) {
		super(n);
		count = 1;
	};

	protected AbsTree add_node(int n) {
		return new DupTree(n);
	}

	protected void count_duplicates() {
		count++;
	}

	protected int get_count() {
		// to be filled by you
		return count;
	}

	protected void set_count(int v) {
		count = v;
	}

	protected int count;

}
