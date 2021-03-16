import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BST_DupTree_Test {

	static DupTree dtr;
	static List<Integer> al = new ArrayList<Integer>();
	static Random r = new Random();

	// add annotation
	@BeforeAll
	public static void setup() {
		// to be filled in by you
		int val = r.nextInt(25);
		dtr = new DupTree(val);
		al.add(val);
		for (int i = 0; i < 25; i++) {
			val = r.nextInt(25);
			dtr.insert(val);
			al.add(val);
		}
		Collections.sort(al);
		System.out.println("DupTree created in Setup: ");
		Iterator<Integer> itr2 = al.iterator();
		Iterator<Integer> itr1 = dtr.iterator();
		while (itr1.hasNext()) {
			System.out.print(itr1.next() + " ");
		}
		System.out.println();
		System.out.println("\nSorted ArrayList created in Setup: ");
		while (itr2.hasNext()) {
			System.out.print(itr2.next() + " ");
		}
		System.out.println();
	}

	// add annotation
	@BeforeEach
	@AfterEach
	void check_invariant() {
		// to be filled in by you
		assertTrue(ordered(dtr));
		System.out.println();
		System.out.println("----------------------------");
		System.out.println("DupTree invariant maintained");
		System.out.println("----------------------------");
	}

	// add annotation
	@Test
	void test_delete() {
		// to be filled in by you
		int v;
		System.out.println();
		for (int i = 0; i < 10; i++) {
			v = r.nextInt(25);
			System.out.println("Testing DupTree delete(" + v + ")");
			dtr.insert(v);
			int old_count = get_count(dtr, v);
			System.out.println("inserted value " + v + " into duptree; count = " + old_count + " after insertion.");
			dtr.delete(v);
			int new_count = get_count(dtr, v);
			if (old_count > 1) {
				assertTrue(new_count == old_count - 1);
				System.out.println("deleted value " + v + " into duptree; count = " + new_count + " after deletion.");
			} else {
				assertTrue(new_count == 0);
				assertTrue(dtr.find(v) == null);
				System.out.println("deleted value " + v + " into duptree; it is no longer present in duptree");
			}
			System.out.println();
		}
		System.out.println("DupTree delete test passed");
	}

	// add annotation
	@Test
	void test_insert() {
		// to be filled in by you
		System.out.println("\n Testing DupTree insert ...");
		Iterator<Integer> itr1 = al.iterator();
		Iterator<Integer> itr2 = dtr.iterator();
		System.out.println("Creating ArrayList iterator and Comparing elements pair-wise ...");
		while (itr1.hasNext() && itr2.hasNext()) {
			assertTrue(itr1.next() == itr2.next());
		}
		System.out.println("... DupTree insert test passed");
	}

	public int get_count(DupTree tr, int v) {
		// to be filled in by you
		Tree t = tr.find(v);
		if (t != null)
			return t.get_count();
		return 0;
	}

	public boolean ordered(Tree tr) {
		// to be filled in by you
		return (tr.left == null || (tr.value > tr.left.max().value && ordered(tr.left)))
				&& (tr.right == null || (tr.value < tr.right.min().value && ordered(tr.right)));
	}

}
