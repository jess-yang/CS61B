package gitlet;

import java.io.File;
import java.io.Serializable;

/**Blob class that represents an individual file. */
public class Blob implements Serializable {

    /** Blob constructor from a name.
     * @param name*/
    public Blob (String name) {
        _name = name;
        File blobContent = new File(Checkout.CWD, name);
        _data = Utils.readContentsAsString(blobContent);
        _sha1 = Utils.sha1(_data);
    }

    /** Getter function for name. */
    public String getName() {
        return _name;
    }

    /** Getter function for blob sha1. */
    public String getSha1() {
        return _sha1;
    }

    /** Getter function for blob data as string. */
    public String getData() {
        return _data;
    }



    private String _name;
    private String _sha1;
    private String _data;
}
