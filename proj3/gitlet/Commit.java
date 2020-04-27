package gitlet;

import java.io.Serializable;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Commit implements Serializable {

    public Commit(String time, String message, HashMap<String, Blob> blob) {
        _time = time;
        _message = message;
        _blob = blob;
    }

    String time = new SimpleDateFormat("hh:mm:ss z, EEE d MMM yyyy").format(new Date(0));

    HashMap<String, Blob> empty = new HashMap();

    Commit initialCommitObj = new Commit(time, "initial message", empty);
    String sha1 = Utils.sha1(initialCommitObj);

    Tree commits = new Tree(initialCommitObj, initialCommitObj); //fixme implement tree?

    private String _time;
    private String _message;
    private HashMap _blob;
    private String _sha1;
}
