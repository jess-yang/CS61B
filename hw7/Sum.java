import java.util.Arrays;
import java.util.Collections;

/** HW #7, Two-sum problem.
 * @author Jessica Yang
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        for (int each : A) {
            int left = m - each;
            for (int beach : B) {
                if (each + beach == m) {
                    return true;
                }
            }
        }
        return false;  // REPLACE WITH YOUR ANSWER
    }

}
