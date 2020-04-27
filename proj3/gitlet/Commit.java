package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Commit implements Serializable {


    public Commit(String message) {
        _message = message;
        _time = new SimpleDateFormat("E MMM d HH:mm:ss y Z")
                .format(new Date());
        _parent = findPreviousCommit();
        _parentsha1 = findPreviousCommit()._sha1;
        _blob = updatedBlobs();
        _sha1 = Utils.sha1(this);

    }

    public void commitAction() throws IOException {
        if (new File(".gitlet/stage/add").length() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }


        // add to commit folder
        File newCommit = new File(".gitlet/commits/" + _sha1);
        newCommit.createNewFile();
        Utils.writeObject(newCommit,this);
        // fixme head file needs to be changed?


        // fixme change branch content to be this ID
        File thisBranch = new File(".gitlet/branches/" + )

        //clear stage folders (add and remove)
        for (String fileName : Utils.plainFilenamesIn(".gitlet/stage/add")) {
            Utils.restrictedDelete(fileName);
        }
        for (String fileName : Utils.plainFilenamesIn(".gitlet/stage/remove")) {
            Utils.restrictedDelete(fileName);
        }


    }

    /**Returns an updated HashMap with Key/Blob values that we are currently keeping track of. */
    public HashMap<String, Blob> updatedBlobs() {

        HashMap ret = deepCopy(_parent._blob);
        for (String addFileName : Utils.plainFilenamesIn(".gitlet/stage/add")) {
            ret.put(addFileName, Utils.readObject(new File(".gitlet/stage/add/" + addFileName), Blob.class));
        }
        for (String removeFileName : Utils.plainFilenamesIn(".gitlet/stage/remove")) {
            ret.remove(removeFileName);
        }

        return ret;
    }

    /** Makes a deep copy of the Hashmap for the purpose of updating tracked files. */
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
        String headCommitinBranch = Utils.readContentsAsString(new File(".gitlet/branches/"+headBranch));
        Commit lastCommit = Utils.readObject(new File(".gitlet/commits/"+headCommitinBranch), Commit.class);

        return lastCommit;

    }

    /** Get method for Blob HashMap. **/
    public HashMap getBlob() {
        return _blob;
    }



    private String _time;
    private String _message;
    private HashMap _blob;
    private String _sha1;
    private String _parentsha1;
    private Commit _parent;
}
