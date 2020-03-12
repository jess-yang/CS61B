package enigma;

import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Jessica Yang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _plugboard = null;
        _rotorList = new Rotor[numRotors()];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        HashMap<String, Rotor> allRotorsDict = new HashMap<>();
        for (Rotor r : _allRotors) {
            allRotorsDict.put(r.name(), r);
        }

        for (int i = 0; i < rotors.length; i++) {
            _rotorList[i] = allRotorsDict.get(rotors[i]);
        }
        if (!(_rotorList[0].reflecting())) {
            throw error("leftmost not a reflector");
        }
        int movingRotors = 0;
        for (Rotor r : _rotorList) {
            if (r instanceof MovingRotor) {
                movingRotors++;
            }
        }
        if (movingRotors != numPawls()) {
            throw error("qty of Pawls and moving rotors do not match");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("incorrect number of rotor settings");
        }
        for (int i = 1; i < numRotors(); i++) {
            _rotorList[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] atNotch = new boolean[numRotors()];
        for (int i = numRotors() - 1; i > 0; i--) {
            if (_rotorList[i].atNotch()) {
                atNotch[i] = true;
            }
        }
        _rotorList[numRotors() - 1].advance();
        boolean[] isAdvanced = new boolean[numRotors()];
        isAdvanced[numRotors() - 1] = true;

        for (int i = numRotors() - 1; i > 0; i--) {
            if (atNotch[i] && _rotorList[i - 1].rotates()
                    && !isAdvanced[i - 1]) {
                _rotorList[i - 1].advance();
                isAdvanced[i - 1] = true;
                if (!isAdvanced[i]) {
                    _rotorList[i].advance();
                    isAdvanced[i] = true;
                }
            }
        }

        int result = c;
        if (_plugboard != null) {
            result = _plugboard.permute(result);
        }
        for (int i = numRotors() - 1; i >= 0; i--) {
            result = _rotorList[i].convertForward(result);
        }
        for (int i = 1; i < numRotors(); i++) {
            result = _rotorList[i].convertBackward(result);
        }
        if (_plugboard != null) {
            result = _plugboard.permute(result);
        }

        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll("\\s", "");
        String output = "";
        for (int i = 0; i < msg.length(); i++) {
            int inputIndex = _alphabet.toInt(msg.charAt(i));
            output += _alphabet.toChar(convert(inputIndex));
        }
        return output;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of rotors. */
    private int _numRotors;
    /** Number of pawls (i.e. moving rotors). */
    private int _pawls;
    /** Collection of all rotors. */
    private Collection<Rotor> _allRotors;
    /** List of rotors in my setup.*/
    private Rotor[] _rotorList;
    /** Permutation of plugboard. */
    private Permutation _plugboard;

}
