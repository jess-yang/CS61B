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
        //account for specific order of rotors?
        HashMap<String, Rotor> allRotorsDict = new HashMap<>();
        for (Rotor r : _allRotors) {
            allRotorsDict.put(r.name(), r);
        }
        _rotorList = new Rotor[numRotors()];
        int i = 0;
        for (String name : rotors) {
            _rotorList[i] = allRotorsDict.get(name);
            i++;
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _rotorList[i+1]._setting = setting.toCharArray()[i];
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        for (int i = 0; i < plugboard.size(); i++) {
            if (_plugboard.cycles().charAt(i) == '(') {
                int count = 0;
                for (int j = i+1; _plugboard.cycles().charAt(j) != ')'; j++) {
                    if (_plugboard.cycles().charAt(j) != ' ' || _plugboard.cycles().charAt(j) != ')') {
                        count++;
                    }
                }
                if (count > 2) {
                    throw error("plugboard permutes(routes) more than 2 letters to each other");
                }
            }
        }
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        //advance shit / at notch??

        int result = c;
        if (_plugboard != null) {
            result = _plugboard.permute(result);
        }
        for (int i = numRotors()-1; i >= 0; i--) {
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
        char[] input = msg.trim().toCharArray();
        String output = "";
        for (int i = 0; i < input.length; i++) {
            int inputIndex = _alphabet.toInt(input[i]);
            output += _alphabet.toChar(convert(inputIndex));
        }
        return output;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    private int _numRotors;
    private int _pawls;
    private Collection<Rotor> _allRotors;
    private Rotor[] _rotorList;
    private Permutation _plugboard;

}
