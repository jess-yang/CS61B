package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Jessica Yang
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }
    @Test
    public void testSize(){
        Permutation p = getNewPermutation("(BEACD)", getNewAlphabet("ABCDE"));
        assertEquals(5,p.size());

        Permutation p1 = getNewPermutation("", getNewAlphabet(""));
        assertEquals(0,p1.size());

        Permutation p2 = getNewPermutation("(ABC)", getNewAlphabet("ABCDE"));
        assertEquals(3,p2.size());
    }
    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('A', p.invert('C'));
        assertEquals('C', p.invert('D'));
        assertEquals(1,p.invert(0));
        assertEquals(2,p.invert(3));

        Permutation p1 = getNewPermutation("", getNewAlphabet("JKLM"));
        assertEquals('K', p1.invert('K'));
        assertEquals(0, p1.invert(0));
    }
    @Test
    public void testPermuteChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('C', p.permute('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));
        assertEquals(2,p.permute(0));
        assertEquals(1,p.permute(3));

        Permutation p1 = getNewPermutation("", getNewAlphabet("JKLM"));
        assertEquals('K', p1.permute('K'));
        assertEquals(0, p1.permute(0));
    }
    @Test
    public void testAlphabet() {
        Alphabet a = getNewAlphabet("JESICA");
        Permutation p = getNewPermutation("(JSEIAC)", a);
        assertEquals(a, p.alphabet());

        Alphabet a1 = getNewAlphabet("THEAU");
        Permutation p1 = getNewPermutation("", a1);
        assertEquals(a1, p1.alphabet());
    }
    @Test
    public void testDerangement() {

        Alphabet a1 = getNewAlphabet("THEAU");
        Permutation p1 = getNewPermutation("(TEAUH)", a1);
        assertTrue(p1.derangement());

        Alphabet a2 = getNewAlphabet("ABCD");
        Permutation p2 = getNewPermutation("", a2);
        assertFalse(p2.derangement());

        Alphabet a3 = getNewAlphabet("ABCD");
        Permutation p3 = getNewPermutation("(AB)", a2);
        assertFalse(p3.derangement());


    }

}
