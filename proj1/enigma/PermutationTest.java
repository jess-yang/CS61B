package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;


    Permutation getNewPermutation(String cycles, Alphabet alphabet) {
        return new Permutation(cycles, alphabet);
    }
    Alphabet getNewAlphabet(String chars) {
        return new Alphabet(chars);
    }
    Alphabet getNewAlphabet() {
        return new Alphabet();
    }

    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */
    @Test
    public void testSize() {
        Permutation p = getNewPermutation("(BEACD)", getNewAlphabet("ABCDE"));
        assertEquals(5, p.size());

        Permutation p1 = getNewPermutation("", getNewAlphabet(""));
        assertEquals(0, p1.size());

        Permutation p2 = getNewPermutation("(ABC)", getNewAlphabet("ABCDE"));
        assertEquals(5, p2.size());

        Permutation p3 = getNewPermutation("(!@) (31)", getNewAlphabet("31!@"));
        assertEquals(4, p3.size());

        Permutation p4 = getNewPermutation("   (ABC)  (DE)          (FGHI)    ",
                getNewAlphabet("ABCDEFGHI"));
        assertEquals(9, p4.size());

    }

    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('A', p.invert('C'));
        assertEquals('C', p.invert('D'));
        assertEquals(1, p.invert(0));
        assertEquals(2, p.invert(3));

        Permutation p1 = getNewPermutation("", getNewAlphabet("JKLM"));
        assertEquals('K', p1.invert('K'));
        assertEquals(0, p1.invert(0));

        Permutation p2 = getNewPermutation("(JKLM)", getNewAlphabet("JKLM"));
        assertEquals('M', p2.invert('J'));
        assertEquals(3, p2.invert(0));

        Permutation p3 = getNewPermutation(" (A)  ", getNewAlphabet("A"));
        assertEquals('A', p3.invert('A'));
        assertEquals(0, p3.invert(0));

        Permutation p4 = getNewPermutation("(!32@)", getNewAlphabet("!32@"));
        assertEquals('@', p4.invert('!'));
        assertEquals(3, p4.invert(0));
    }

    @Test
    public void testPermuteChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('C', p.permute('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));
        assertEquals(2, p.permute(0));
        assertEquals(1, p.permute(3));

        Permutation p1 = getNewPermutation("", getNewAlphabet("JKLM"));
        assertEquals('K', p1.permute('K'));
        assertEquals(0, p1.permute(0));

        Permutation p2 = getNewPermutation(" (JK) (LM)",
                getNewAlphabet("JKLM"));
        assertEquals('L', p2.permute('M'));
        assertEquals(2, p2.permute(3));

        Permutation p4 = getNewPermutation("(!32@)", getNewAlphabet("!32@"));
        assertEquals('3', p4.permute('!'));
        assertEquals(1, p4.permute(0));
    }

    @Test
    public void testAlphabet() {
        Alphabet a = getNewAlphabet("JESICA");
        Permutation p = getNewPermutation("(JSEIAC) ", a);
        assertEquals(a, p.alphabet());

        Alphabet a1 = getNewAlphabet("THEAU");
        Permutation p1 = getNewPermutation("", a1);
        assertEquals(a1, p1.alphabet());

        Alphabet a2 = getNewAlphabet("!23@");
        Permutation p2 = getNewPermutation("(!3) (2@) ", a2);
        assertEquals(a2, p2.alphabet());
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
        Permutation p3 = getNewPermutation("(ABC)(D)", a3);
        assertFalse(p3.derangement());


    }

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }


}
