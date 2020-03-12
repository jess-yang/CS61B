package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Jessica Yang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    private String _notches;

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        this.set(0);
    }

    @Override
    void advance() {
        set((_setting + 1) % size());
    }

    /** Return true iff I have a ratchet and can move. */
    @Override
    boolean rotates() {
        return true;
    }

    @Override
    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        char settingChar = alphabet().toChar(setting());
        if (_notches.contains(Character.toString(settingChar))) {
            return true;
        }
        return false;


    }

}
