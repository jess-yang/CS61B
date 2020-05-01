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

    /** Getter function for name.
     * @return name*/
    public String getName() {
        return _name;
    }

    /** Getter function for blob sha1.
     * @return sha1 */
    public String getSha1() {
        return _sha1;
    }

    /** Getter function for blob data as string.
     * return data*/
    public String getData() {
        return _data;
    }


    /**Name of blob file. */
    private String _name;
    /**String sha1 code of blob. */
    private String _sha1;
    /**String of blob data. */
    private String _data;
}
