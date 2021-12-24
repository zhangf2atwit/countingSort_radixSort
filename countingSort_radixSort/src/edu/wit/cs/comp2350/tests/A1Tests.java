package edu.wit.cs.comp2350.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.Permission;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.A1;

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class A1Tests{

	@Rule
	public Timeout globalTimeout = Timeout.seconds(15);

	@SuppressWarnings("serial")
	private static class ExitException extends SecurityException {}

	private static class NoExitSecurityManager extends SecurityManager 
	{
		@Override
		public void checkPermission(Permission perm) {}

		@Override
		public void checkPermission(Permission perm, Object context) {}

		@Override
		public void checkExit(int status) { super.checkExit(status); throw new ExitException(); }
	}

	@Before
	public void setUp() throws Exception 
	{
		System.setSecurityManager(new NoExitSecurityManager());
	}

	@After
	public void tearDown() throws Exception 
	{
		System.setSecurityManager(null);
	}

	private void _test(int[] values, int[] expected, char algo) {

		int[] actual = new int[0];

		try {
			if (algo == 'c')
				actual = A1.countingSort(values);
			else
				actual = A1.radixSort(values);
		} catch (ExitException e) {
			assertTrue("Program crashed", false);
		}
		assertNotNull("Null output.", actual);
		assertEquals("Output has an incorrect number of items.", expected.length, actual.length);

		if (actual.length > 20) {
			for (int i = 0; i < actual.length; i++)
				assertEquals("Mismatch in position " + i + ".", expected[i], actual[i]);
		}
		else {
			for (int i = 0; i < actual.length; i++)
				assertEquals("Expected array " + Arrays.toString(expected) + " but got " + Arrays.toString(actual) + ".", expected[i], actual[i]);
		}

	}

	private int[] generateRandArray(int size) {
		int[] ret = new int[size];

		Random r = new Random();
		for (int i = 0; i < size; i++) {
			ret[i] = r.nextInt(A1.MAX_INPUT+1);
		}
		return ret;
	}

	private void testRand(char c, int size) {
		int[] randArray = generateRandArray(size);
		int[] sortedArray = Arrays.copyOf(randArray, size);
		Arrays.sort(sortedArray);

		_test(randArray, sortedArray, c);
	}

	@Test
	public void test00_EmptyCounting() {
		_test(new int[0], new int[0], 'c');
	}

	@Test
	public void test01_SingleCounting() {
		_test(new int[] {1}, new int[] {1}, 'c');
		_test(new int[] {10000}, new int[] {10000}, 'c');
	}

	@Test
	public void test02_SmallCounting() {
		_test(new int[] {1, 2, 3}, new int[] {1, 2, 3}, 'c');
		_test(new int[] {3, 2, 1}, new int[] {1, 2, 3}, 'c');
		_test(new int[] {1, 2, 3, 4}, new int[] {1, 2, 3, 4}, 'c');
		_test(new int[] {3, 2, 1, 4}, new int[] {1, 2, 3, 4}, 'c');
		_test(new int[] {2, 1}, new int[] {1, 2}, 'c');
		_test(new int[] {9999, 10000}, new int[] {9999, 10000}, 'c');
		_test(new int[] {10000, 9999}, new int[] {9999, 10000}, 'c');
	}

	@Test
	public void test03_RandCounting() {
		for (int i = 0; i < 10; i++)
			testRand('c', 1000);
	}

	@Test
	public void test04_SizesCounting() {
		_test(new int[] {1, 10, 100, 1000, 10000, 100000}, new int[] {1, 10, 100, 1000, 10000, 100000}, 'c');
		_test(new int[] {100000, 10000, 1000, 100, 10, 1}, new int[] {1, 10, 100, 1000, 10000, 100000}, 'c');
		_test(new int[] {10000, 10, 1, 1000, 100, 100000}, new int[] {1, 10, 100, 1000, 10000, 100000}, 'c');
	}

	@Test
	public void test05_EmptyRadix() {
		_test(new int[0], new int[0], 'r');
	}

	@Test
	public void test06_SingleRadix() {
		_test(new int[] {1}, new int[] {1}, 'r');
		_test(new int[] {10000}, new int[] {10000}, 'r');
	}

	@Test
	public void test07_SmallRadix() {
		_test(new int[] {1, 2, 3}, new int[] {1, 2, 3}, 'r');
		_test(new int[] {3, 2, 1}, new int[] {1, 2, 3}, 'r');
		_test(new int[] {1, 2, 3, 4}, new int[] {1, 2, 3, 4}, 'r');
		_test(new int[] {3, 2, 1, 4}, new int[] {1, 2, 3, 4}, 'r');
		_test(new int[] {2, 1}, new int[] {1, 2}, 'r');
		_test(new int[] {9999, 10000}, new int[] {9999, 10000}, 'r');
		_test(new int[] {10000, 9999}, new int[] {9999, 10000}, 'r');
	}

	@Test
	public void test08_RandRadix() {
		testRand('r', 1000);
	}

	@Test
	public void test09_SizesRadix() {
		_test(new int[] {1, 10, 100, 1000, 10000, 100000}, new int[] {1, 10, 100, 1000, 10000, 100000}, 'r');
		_test(new int[] {100000, 10000, 1000, 100, 10, 1}, new int[] {1, 10, 100, 1000, 10000, 100000}, 'r');
		_test(new int[] {10000, 10, 1, 1000, 100, 100000}, new int[] {1, 10, 100, 1000, 10000, 100000}, 'r');
	}

	@Test
	public void test10_PublicMethods() {
		List<String> mNames = Arrays.asList("countingSort", "radixSort", "systemSort", "insertionSort",
				"main", "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll");

		for (Method m: A1.class.getMethods())
			assertTrue("method named " + m.getName() + " should be private.",
					Modifier.isPrivate(m.getModifiers()) || mNames.contains(m.getName()));		
	}

}
