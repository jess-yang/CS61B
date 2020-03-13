package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Jessica Yang
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return my current ring setting. */
    int ringSetting() {
        return _ringSetting;
    }
    /** Set setting() to POSN.
     * @param cposn  */
    void ringSet(char cposn) {
        _ringsUsed = true;
        _ringSetting = alphabet().toInt(cposn);
    }

    /** Set setting() to POSN.
     * @return _ringsUsed*/
    boolean ringsUsed() {
        return _ringsUsed;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        int cposnInt = alphabet().toInt(cposn);
        _setting = cposnInt;
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        if (ringsUsed()) {
            p = p - _ringSetting;
        }
        int adjustedSetting = _permutation.wrap(p + _setting);
        int afterPerm = _permutation.permute(adjustedSetting);
        int dest = afterPerm - _setting;
        int adjustedDest = _permutation.wrap(dest);
        if (ringsUsed()) {
            adjustedDest = _permutation.wrap(adjustedDest + _ringSetting);
        }
        return adjustedDest;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        if (ringsUsed()) {
            e = e - _ringSetting;
        }
        int adjustedSetting = _permutation.wrap(e + _setting);
        int afterInv = _permutation.invert(adjustedSetting);
        int dest = afterInv - _setting;
        int adjustedDest = _permutation.wrap(dest);
        if (ringsUsed()) {
            adjustedDest = _permutation.wrap(adjustedDest + _ringSetting);
        }
        return adjustedDest;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** My setting. */
    private int _setting;

    /** My ring setting. */
    private int _ringSetting;

    /** Whether or not rings were used. */
    private boolean _ringsUsed = false;

}
