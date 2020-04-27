package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Commit implements Serializable {

    /**Initial Commit constructor. _parent is null, and _blob is an empty HashMap. */
    public Commit() {
        _message = "initial commit";
        _time = new SimpleDateFormat("E MMM d HH:mm:ss y Z")
                .format(new Date());
        _parent = null;
        _parentsha1 = null;
        _blob = new HashMap<String, Blob>();

        _sha1 = Utils.sha1(Utils.serialize(this));
    }


    /** Commit constructor. */
    public Commit(String message) {
        _message = message;
        _time = new SimpleDateFormat("E MMM d HH:mm:ss y Z")
                .format(new Date());
        _parent = findPreviousCommit();
        _parentsha1 = findPreviousCommit()._sha1;
        _blob = updatedBlobs();
        _sha1 = Utils.sha1(Utils.serialize(this));


    }

    /** The action of committing something. */
    public void commitAction() throws IOException {
        //if (new File(".gitlet/stage/add").length() == 0) {
        if (Init.ADD_STAGE.length() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        // add to commit folder
        File newCommit = new File(".gitlet/commits/" + _sha1);
        newCommit.createNewFile();
        Utils.writeObject(newCommit,this);



        // change branch content to be this ID
        String head = Utils.readContentsAsString(new File(".gitlet/head"));
        File thisBranch = new File(".gitlet/branches/" + head);
        Utils.writeContents(thisBranch, this._sha1);

        //clear stage folders (add and remove)
        for (String fileName : Utils.plainFilenamesIn(".gitlet/stage/add")) {
            Utils.restrictedDelete(fileName);
        }
        for (String fileName : Utils.plainFilenamesIn(".gitlet/stage/remove")) {
            Utils.restrictedDelete(fileName);
        }


    }

    /**Returns an updated HashMap with Key/Blob pairs that we are currently keeping track of. */
    public HashMap<String, Blob> updatedBlobs() {
        HashMap<String, Blob> ret = deepCopy(_parent._blob);
        for (String addFileName : Utils.plainFilenamesIn(".gitlet/stage/add")) {
            ret.put(addFileName, Utils.readObject(new File(".gitlet/stage/add/" + addFileName), Blob.class));
        }
        for (String removeFileName : Utils.plainFilenamesIn(".gitlet/stage/remove")) {
            ret.remove(removeFileName);
        }

        return ret;
    }

    /** Makes a deep copy of the HashMap for the purpose of updating tracked files.*/
    public HashMap<String, Blob> deepCopy(HashMap<String, Blob> original) {
        HashMap<String, Blob> copy = new HashMap<>();
        for (HashMap.Entry<String, Blob> entry : original.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    /** Static method for finding the previous commit, according to head commit. **/
    public static Commit findPreviousCommit() {
        String headBranch = Utils.readContentsAsString(new File(".gitlet/head"));
        String headCommitinBranch = Utils.readContentsAsString(new File(".gitlet/branches/" + headBranch));
        Commit lastCommit = Utils.readObject(new File(".gitlet/commits/" + headCommitinBranch), Commit.class);

        return lastCommit;

    }

    /** Get method for Blob HashMap. **/
    public HashMap<String, Blob> getBlob() {
        return _blob;
    }

    /** Get method for SHA1 code. **/
    public String getSHA1() {
        return _sha1;
    }

    /** Get method for parent.**/
    public Commit getParent() {
        return _parent;
    }

    /** Get method for parent's SHA1 code.**/
    public String getParentSHA1() {
        return _parentsha1;
    }

    /** Get method for time.**/
    public String getTime() {
        return _time;
    }

    /** Get method for message.**/
    public String getMessage() {
        return _message;
    }


    private String _time;
    private String _message;
    private HashMap<String, Blob> _blob;
    private String _sha1;
    private String _parentsha1;
    private Commit _parent;
    private String _branch;
}
