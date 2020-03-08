package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jessica Yang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    private String _cycles;

    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles = _cycles + "(" + cycle + ")";
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int adjustedP= wrap(p);
        char target = _alphabet.toChar(adjustedP);
        String targetString = Character.toString(target);
        if (_cycles.contains(targetString)){
            for (int i = 0; i < _cycles.length(); i++) {
                if (_cycles.charAt(i) == target) {
                    if (_cycles.charAt(i+1) == ')') {
                        for (int j = i; j > 0; j--) {
                            if (_cycles.charAt(j-1) == '(') {
                                target = _cycles.charAt(j);
                                return _alphabet.toInt(target);
                            }
                        }
                    }
                    else {
                        target = _cycles.charAt(i+1);
                        return _alphabet.toInt(target);
                    }
                }
            }
        }
        return adjustedP;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int adjustedC= wrap(c);
        char target = _alphabet.toChar(adjustedC);
        if (_cycles.contains(Character.toString(target))){
            for (int i = 0; i < _cycles.length(); i++) {
                if (_cycles.charAt(i) == target) {
                    if (_cycles.charAt(i-1) == '(') {
                        for (int j = i; j > 0; j++) {
                            if (_cycles.charAt(j+1) == ')') {
                                target = _cycles.charAt(j);
                                return _alphabet.toInt(target);
                            }
                        }
                    }
                    else {
                        target = _cycles.charAt(i-1);
                        return _alphabet.toInt(target);
                    }
                }
            }
        }
        return adjustedC;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int pIndex = _alphabet.toInt(p);
        return _alphabet.toChar(permute(pIndex));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int cIndex = _alphabet.toInt(c);
        return _alphabet.toChar(invert(cIndex));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (!_cycles.contains(Character.toString(_alphabet.toChar(i)))) {
                return false;
            }
        }
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == '(') {
                int count = 0;
                for (int j = i+1; _cycles.charAt(j) != ')'; j++) {
                    if (_cycles.charAt(j) != ' ' || _cycles.charAt(j) != ')') {
                        count++;
                    }
                }
                if (count == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
}
