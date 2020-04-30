package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {

    /** Blob constructor from a name. */
    public Blob (String name) {
        _name = name;
        File blobContent = new File(Checkout.CWD, name);
        _data = Utils.readContentsAsString(blobContent);
        _sha1 = Utils.sha1(_data);
    }

    public String getName() {
        return _name;
    }

    public String getSha1() {
        return _sha1;
    }

    public String getData() {
        return _data;
    }



    private String _name;
    private String _sha1;
    private String _data;
}
