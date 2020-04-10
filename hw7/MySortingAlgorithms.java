import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; i++) {
                int index = i;
                int value = array[i];
                while (index > 0 && array[index-1] > value ) {
                    array[index] = array[index-1];
                    index--;
                }
                array[index] = value;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                int smallest = array[i];
                int smallestIndex = i;
                int testIndex = i;
                while (testIndex < k) {
                    if (array[testIndex] < smallest) {
                        smallest = array[testIndex];
                        smallestIndex = testIndex;
                    }
                    testIndex++;
                }
                array[smallestIndex] = array[i];
                array[i] = smallest;
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k > 1) {
                int half = k / 2;
                int[] right = new int[k - half];
                int[] left = new int[half];
                int i = 0;
                while (i < half) {
                    left[i] = array[i];
                    i++;
                }
                int z = 0;
                while (i < k) {
                    right[z] = array[i];
                    z++;
                    i++;
                }
                sort(left, left.length);
                sort(right, right.length);
                merge(array, left, right);
            }
        }
        private void merge(int[] finalArray, int[] left, int[] right) {
            int i = 0;
            int iLeft = 0;
            int iRight = 0;
            while (iLeft < left.length && iRight < right.length) {
                if (left[iLeft] > right[iRight]) {
                    finalArray[i] = right[iRight];
                    i++;
                    iRight++;
                } else {
                    finalArray[i] = left[iLeft];
                    i++;
                    iLeft++;
                }
            }
            if (iRight < right.length) {
                while (iRight < right.length) {
                    finalArray[i] = right[iRight];
                    i++;
                    iRight++;
                }
            }
            if (iLeft < left.length) {
                while (iLeft < left.length) {
                    finalArray[i] = left[iLeft];
                    i++;
                    iLeft++;
                }
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {

            ArrayList<Integer> ones = new ArrayList<>();
            ArrayList<Integer> zeroes = new ArrayList<>();
            int index = k;
            while (index > 0) {
                if ((a[index-1] & 1) == 0) {
                    zeroes.add(a[index-1]);
                } else {
                    ones.add(a[index-1]);
                }
                index--;
            }

            int BITS = 32;
            for (int p = 1; p < BITS; p++) {
                int power = (int) Math.pow(2, p);
                ArrayList<Integer> newOnes = new ArrayList<>();
                ArrayList<Integer> newZeroes = new ArrayList<>();
                for (int one : ones) {
                    if ((one & power) == 0) {
                        newZeroes.add(one);
                    } else {
                        newOnes.add(one);
                    }
                }
                for (int zero : zeroes) {
                    if ((zero & power) == 0) {
                        newZeroes.add(zero);
                    } else {
                        newOnes.add(zero);
                    }
                }
                ones = newOnes;
                zeroes = newZeroes;
            }

            index = 0;
            for (int i = zeroes.size()-1; i >= 0; i--) {
                a[index] = zeroes.get(i);
                index++;
            }
            for (int i = ones.size()-1; i >= 0; i--) {
                a[index] = ones.get(i);
                index++;
            }
        }

         @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
