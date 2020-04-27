package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {

    public Blob (String name) {
        _name = name;
        _data = Utils.readContents(new File(name));
        _sha1 = Utils.sha1(_data);
    }

    public String getName() {
        return _name;
    }

    public String getSha1() {
        return _sha1;
    }

    public byte[] getData() {
        return _data;
    }



    private String _name;
    private String _sha1;
    private byte[] _data;
}
