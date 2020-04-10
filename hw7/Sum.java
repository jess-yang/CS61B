import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/** HW #7, Two-sum problem.
 * @author Jessica Yang
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        HashSet<Integer> values = new HashSet<Integer>();
        for (int i = 0; i < B.length; i++) {
            values.add(B[i]);
        }
        for (int i = 0; i < A.length; i++) {
            int each = A[i];
            int left = m - each;
            if (values.contains(left)) {
                return true;
            }
        }
        return false;  // REPLACE WITH YOUR ANSWER
    }

}
