import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Jessica Yang
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    Reader str;
    String from;
    String to;
    public TrReader(Reader str, String from, String to) {
        this.str=str;
        this.from=from;
        this.to=to;
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
    @Override
    public int read(char[] buf, int offset, int count) throws IOException {
        int read = str.read(buf, offset, count);

        int i = 0;
        while(i < buf.length) {
            for (int j = 0; j < from.length(); j++) {
                if (from.charAt(j) == buf[i]) {
                    buf[i] = to.charAt(j);
                    break;
                }
            }
            i++;
        }
        return read;

    }

    @Override
    public void close() throws IOException{
        str.close();
    }
}
