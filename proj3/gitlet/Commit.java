package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/** Class for commit.
 *  @author Jessica Yang
 */
public class Commit implements Serializable {

    /**Initial Commit constructor. _parent is null,
     * and _blob is an empty HashMap.*/
    public Commit() {
        _message = "initial commit";
        _time = new SimpleDateFormat("E MMM d HH:mm:ss y Z")
                .format(new Date());
        _parentsha1 = null;
        _blob = new HashMap<String, Blob>();

        _sha1 = "0000000000000000000000000000000000000000";
    }

    /**Merge conflict constructor.
     * @param parent1 the first parent
     * @param message the Commit message
     * @param parent2 the second parent*/
    public Commit(Commit parent1, Commit parent2, String message) {
        _message = message;
        _time = new SimpleDateFormat("E MMM d HH:mm:ss y Z")
                .format(new Date());
        _parentsha1 = parent1._sha1;
        _parent2sha1 = parent2._sha1;
        _blob = updatedBlobs();
        _sha1 = Utils.sha1(Utils.serialize(this));
    }

    /** Commit constructor.
     * @param message Commit message*/
    public Commit(String message) {
        _message = message;
        _time = new SimpleDateFormat("E MMM d HH:mm:ss y Z")
                .format(new Date());
        _parentsha1 = findPreviousCommit()._sha1;
        _blob = updatedBlobs();
        _sha1 = Utils.sha1(Utils.serialize(this));
    }

    /** The action of committing something. */
    public void commitAction() throws IOException {
        List addFileList = Utils.plainFilenamesIn(Init.ADD_STAGE);
        List removeFileList = Utils.plainFilenamesIn(Init.REMOVE_STAGE);
        if (addFileList.size() == 0 && removeFileList.size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        File newCommit = new File(".gitlet/commits/" + _sha1);
        newCommit.createNewFile();
        Utils.writeObject(newCommit, this);

        uploadBlobs();

        String head = Utils.readContentsAsString(new File(".gitlet/head"));
        File thisBranch = new File(".gitlet/branches/" + head);
        Utils.writeContents(thisBranch, this._sha1);

        clearStagingArea();
    }
    /** Init's commit action. */
    public void firstCommitAction() throws IOException {
        File newCommit = new File(".gitlet/commits/" + _sha1);
        newCommit.createNewFile();
        Utils.writeObject(newCommit, this);

        String head = Utils.readContentsAsString(new File(".gitlet/head"));
        File thisBranch = new File(".gitlet/branches/" + head);
        Utils.writeContents(thisBranch, this._sha1);
    }

    /**Returns an updated HashMap with Key/Blob pairs
     * that we are currently keeping track of.
     * @return HashMap of updated tracking files*/
    public HashMap<String, Blob> updatedBlobs() {
        HashMap<String, Blob> ret = deepCopy(getParent()._blob);
        for (String addFileName : Utils.plainFilenamesIn(".gitlet/stage/add")) {
            ret.put(addFileName, Utils.readObject(
                    new File(".gitlet/stage/add/" + addFileName), Blob.class));
        }
        for (String removeFileName
                : Utils.plainFilenamesIn(".gitlet/stage/remove")) {
            ret.remove(removeFileName);
        }

        return ret;
    }
    /** saves blobs to BLOBS dir. */
    public static void uploadBlobs() {
        for (String fileName : Init.ADD_STAGE.list()) {
            Blob currBlob = Utils.readObject(
                    new File(".gitlet/stage/add/" + fileName), Blob.class);
            Utils.writeObject(new File(Init.BLOBS, fileName), currBlob);
        }
    }

    /** Clears staging area.*/
    public static void clearStagingArea() {
        for (String fileName : Init.ADD_STAGE.list()) {
            File addFileToDelete = new File(Init.ADD_STAGE, fileName);
            addFileToDelete.delete();
        }
        for (String fileName : Utils.plainFilenamesIn(".gitlet/stage/remove")) {
            File removeFileToDelete = new File(Init.REMOVE_STAGE, fileName);
            removeFileToDelete.delete();
        }

    }


    /** Makes a deep copy of the HashMap for
     * the purpose of updating tracked files.
     * @return copy of HashMap
     * @param original HashMap*/
    public HashMap<String, Blob> deepCopy(HashMap<String, Blob> original) {
        HashMap<String, Blob> copy = new HashMap<>();
        for (HashMap.Entry<String, Blob> entry : original.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }


    /** Static method for finding the previous commit, according to head commit.
     * (Branch is the current one).
     * @return previous commit. **/
    public static Commit findPreviousCommit() {
        String headBranch = Utils.readContentsAsString(
                new File(".gitlet/head"));
        String headCommitinBranch = Utils.readContentsAsString(
                new File(".gitlet/branches/" + headBranch));
        Commit lastCommit = Utils.readObject(
                new File(".gitlet/commits/" + headCommitinBranch),
                Commit.class);

        return lastCommit;

    }

    /** Static method for finding the head commit of a
     * branch with the name of a BRANCHES file.
     * @return head of commit of given branch
     * @param branch branch name. **/
    public static Commit findHeadCommit(String branch) {
        File branchFile = new File(Init.BRANCHES, branch);
        String commitID = Utils.readContentsAsString(branchFile);
        File commitFile = new File(Init.COMMITS, commitID);
        Commit headOfBranch = Utils.readObject(commitFile, Commit.class);
        return headOfBranch;
    }

    /** Get method for Blob HashMap.
     * @return the HashMap of files tracked in this commit.**/
    public HashMap<String, Blob> getBlobs() {
        return _blob;
    }

    /** Get method for SHA1 code.
     * @return this commit's SHA1 code.**/
    public String getSHA1() {
        return _sha1;
    }

    /** Get method for parent.
     * @return parent commit.**/
    public Commit getParent() {
        String parentSha1 = getParentSHA1();
        if (parentSha1 == null) {
            return null;
        }
        File parentFile = new File(Init.COMMITS, parentSha1);
        Commit parent = Utils.readObject(parentFile, Commit.class);
        return parent;
    }

    /** Get method for parent's SHA1 code.
     * @return parent1's SHA1 code.**/
    public String getParentSHA1() {
        return _parentsha1;
    }

    /** Get method for parent2's SHA1 code.
     * @return parent2's SHA1 code.**/
    public Commit getParent2() {
        if (_parent2sha1 == null) {
            return null;
        }
        File parentFile = new File(Init.COMMITS, _parent2sha1);
        Commit parent2 = Utils.readObject(parentFile, Commit.class);
        return parent2;

    }

    /** Get method for time.
     * @return String time.**/
    public String getTime() {
        return _time;
    }

    /** Get method for message.
     * @return Commit message **/
    public String getMessage() {
        return _message;
    }

    /** Instance variable for time.**/
    private String _time;
    /** Instance variable for commit message.**/
    private String _message;
    /** Instance variable for HashMap of blobs.**/
    private HashMap<String, Blob> _blob;
    /** Instance variable for SHA1 code.**/
    private String _sha1;
    /** Instance variable for parent's SHA1 code.**/
    private String _parentsha1;
    /** Instance variable parent2's SHA1 code.**/
    private String _parent2sha1 = null;
    /** Instance variable for parent commit.**/
    private Commit _parent;
    /** Instance variable for this branch.**/
    private String _branch;
}
