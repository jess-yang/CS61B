import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] arr = {{1, 2}, {3, 4}};
        int[][] arr1 = {{3, 2}, {1, -5}};
        assertEquals(4,MultiArr.maxValue(arr));
        assertEquals(3,MultiArr.maxValue(arr1));
    }

    @Test
    public void testAllRowSums() {
        int[][] arr = {{1, 2}, {3, 4}};
        int[] arrResult={3,7};
        int[][] arr1 = {{3, 2}, {1, -5}};
        int[] arr1Result={5,-4};
        int[][] arr2 = {{3, 2}, {1, -5}, {7,10}};
        int[] arr2Result={5,-4,17};
        int[][] arr3 = {{3, 2, 6}, {1, 2}};
        int[] arr3Result={11,3};

        assertArrayEquals(arrResult, MultiArr.allRowSums(arr));
        assertArrayEquals(arr1Result, MultiArr.allRowSums(arr1));
        assertArrayEquals(arr2Result,MultiArr.allRowSums(arr2));
        assertArrayEquals(arr3Result,MultiArr.allRowSums(arr3));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
