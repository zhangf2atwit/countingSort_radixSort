package edu.wit.cs.comp2350;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/** Sorts integers from command line using various algorithms 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Assignment 1
 * 
 */

public class A1 {

	// TODO: document this method
	public static int[] countingSort(int[] a) {
	// TODO: implement this method

		if(a.length == 0) {           // if the there is no input
			int res[] = new int[0];
			return res;
		}
		
		// find the max value in input array to get the size of counter array
		int maxInA = a[0];
		for (int i = 1; i < a.length; i++) {
			if(a[i] > maxInA) {
				maxInA = a[i];
			}
		}
		
	    // Create new arrays for counters and store the sorted array
		int arrCount[] = new int[maxInA + 1];      // get enough position for each element
		int arrSorted[] = new int[a.length];
		
	    // Loop and default all the elements by 0
		for(int i = 0; i < a.length; i++) {
			arrCount[i] = 0;
		}
	
	    // Count how many times the input number appeared of 0,1,2,3,4....100...1000....
		for(int i = 0; i < a.length; i++) {
				arrCount[a[i]]++;
		}
		
		// put input array in order and store them
		int pos = 0;                                  // get the position of a[i] already stored in the nest loops 
		for(int i = 0; i < arrCount.length; i++) {        
			while(arrCount[i]!= 0) {         	
				arrSorted[pos++] = i;                 // make position of stored array increasing, pos++
				arrCount[i]--;
			}   	
		}	
		return arrSorted;	// return an array with sorted values
	}
	
	
	    // TODO: document this method
	public static int[] radixSort(int[] a) {
		// TODO: implement this method

		if(a.length == 0) {
			int res[] = new int[0];
			return res;
		}	
		 int maxInA = a[0];                        // find max value
		 for (int i = 1; i < a.length; i++) {
			if(a[i] > maxInA) {
				maxInA = a[i];
			}
		 }
		 
		 // find max digit
		 int maxDigit = String.valueOf(maxInA).length();
		 
		 for (int i = 0; i < maxDigit; i++) {
			 int[] output = stableDigitSort(a, i);
			 a = output;
		 }
		return a;	// return an array with sorted values
	}

	
	private static int[] stableDigitSort(int[] input, int digit) {
			
		// for all input number x do
		// increment count[digit(x)]
		int[] count = new int[10];
		for (int i = 0; i < 10; i++) {
			count[i] = 0;
		}
		for (int x: input) {
			count[getDigit(digit, x, 10)] ++;
		}
		
		// for x from 0 to k do
		// set pos[x] to number of items < x
		int[] pos = new int[10];
		int accumulation = 0;
		for (int i = 0; i < 10; i ++) {
			pos[i] = accumulation;
			accumulation = accumulation + count[i]; 
		}
		// initiate output
		int[] output = new int[input.length];
		for (int i = 0; i < input.length; i++) {
			output[i] = -1;
		}
		// place x into right position
		// for each input number x (in order) do
		// write x in output array at index pos[digit(x)]
	    // increment pos[digit(x)]

		for (int x: input) {
			int digitNumber = getDigit(digit, x, 10);
			output[pos[digitNumber]] = x;
			pos[digitNumber] ++;	
		}
		return output;
	}
	
	private static int getDigit(int position, int value, int radix) {
        return (int) (value / (int) Math.pow(radix, position)) % radix;  
    } 
	
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	public final static int MAX_INPUT = 262143;
	public final static int MIN_INPUT = 0;

	// example sorting algorithm
	public static int[] insertionSort(int[] a) {

		for (int i = 1; i < a.length; i++) {
			int tmp = a[i];
			int j;
			for (j = i-1; j >= 0 && tmp < a[j]; j--)
				a[j+1] = a[j];
			a[j+1] = tmp;
		}

		return a;
	}

	/**
	 * Implementation note: The sorting algorithm is a Dual-Pivot Quicksort by Vladimir Yaroslavskiy,
	 *  Jon Bentley, and Joshua Bloch. This algorithm offers O(n log(n)) performance on many data 
	 *  sets that cause other quicksorts to degrade to quadratic performance, and is typically 
	 *  faster than traditional (one-pivot) Quicksort implementations.
	 */
	public static int[] systemSort(int[] a) {
		Arrays.sort(a);
		return a;
	}

	// read ints from a Scanner
	// returns an array of the ints read
	private static int[] getInts(Scanner s) {
		ArrayList<Integer> a = new ArrayList<Integer>();

		while (s.hasNextInt()) {
			int i = s.nextInt();
			if ((i <= MAX_INPUT) && (i >= MIN_INPUT))
				a.add(i);
		}

		return toIntArray(a);
	}

	// copies an ArrayList of Integer to an array of int
	private static int[] toIntArray(ArrayList<Integer> a) {
		int[] ret = new int[a.size()];
		for(int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the sorting algorithm to use ([c]ounting, [r]adix, [i]nsertion, or [s]ystem): ");
		char algo = s.next().charAt(0);

		System.out.printf("Enter the integers to sort, followed by a non-integer character: ");
		int[] unsorted_values = getInts(s);
		int[] sorted_values = {};

		s.close();

		switch (algo) {
		case 'c':
			sorted_values = countingSort(unsorted_values);
			break;
		case 'r':
			sorted_values = radixSort(unsorted_values);
			break;
		case 'i':
			sorted_values = insertionSort(unsorted_values);
			break;
		case 's':
			sorted_values = systemSort(unsorted_values);
			break;
		default:
			System.out.println("Invalid sorting algorithm");
			System.exit(0);
			break;
		}

		System.out.println(Arrays.toString(sorted_values));
	}

}
