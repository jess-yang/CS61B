package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Jessica Yang
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    private String _chars;

    /** A personalized alphabet of all upper-case characters.
     * @param chars */
    Alphabet(String chars) {
        for (int i = 0; i < chars.length(); i++) {
            String copy = chars;
            char currentLetter = chars.charAt(i);
            String currLetterString =
                    Character.toString(currentLetter).toUpperCase();
            if (copy.substring(i + 1).contains(currLetterString)) {
                throw new IllegalArgumentException("duplicated chars");
            }
        }
        _chars = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        String chAsString = Character.toString(ch);
        if (_chars.contains(chAsString)) {
            return true;
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        char[] alphabetArray = _chars.toCharArray();
        int count = 0;
        for (int i = 0; i < alphabetArray.length; i++) {
            if (alphabetArray[i] == ch) {
                return i;
            }
        }
        return alphabetArray.length + 1;
    }

}
