package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** FIXME
     */

    @Test
    public void catenateTest(){
        int[] A= {1,2,3};
        int[] B= {1,2,3};
        int[] sol= {1,2,3,1,2,3};
        assertArrayEquals(sol, Arrays.catenate(A,B));
    }
    @Test
    public void removeTest(){
        int[] A= {0,1,2,3,4,5,6};
        int[] sol= {0,1,2,3};
        assertArrayEquals(sol, Arrays.remove(A,4,3));

        int[] sol2= {1,2,3,4,5,6};
        assertArrayEquals(sol2, Arrays.remove(A,0,1));

        int[] sol3= {0,3,4,5,6};
        assertArrayEquals(sol3, Arrays.remove(A,1,2));
    }
    @Test
    public void naturalRunsTest(){
        int[][] sol= {{1, 3, 7}, {5}, {4, 6, 9, 10}};
        int[] A= {1, 3, 7, 5, 4, 6, 9, 10};
        assertArrayEquals(sol, Arrays.naturalRuns(A));
    }

    @Test
    public void catenate2DTest(){
        int[][] sol= {{1, 3, 7}, {5,6}};
        int[] A= {1,3,7};
        int[] B= {5,6};
        assertArrayEquals(Arrays.catenate2D(A,B),sol);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
