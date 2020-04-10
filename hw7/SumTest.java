import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SumTest {
    @Test
    public void sumTest() {
        assertFalse(Sum.sumsTo(new int[]{1,3,5,7}, new int[]{1,2,3,4}, 1000));
        assertTrue(Sum.sumsTo(new int[]{2,4,6,8}, new int[]{1,2,3,4}, 6));
    }
}
