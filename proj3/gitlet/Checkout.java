package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/** Class for checkout, which allows for checkout by filename,
 * filename and commitID, and branch.
 *  @author Jessica Yang
 */
public class Checkout {
    /**File path for the Current Working Directory.*/
    static final File CWD = new File(System.getProperty("user.dir"));

    /**Checkout from the last commit by the file name.
     * @param fileName */
    public static void checkout(String fileName) throws IOException {
        Commit last = Commit.findPreviousCommit();
        HashMap<String, Blob> lastBlobs = last.getBlobs();
        Blob wantedVersion = lastBlobs.get(fileName);
        if (wantedVersion == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            File checkOut = new File(CWD, fileName);
            if (!checkOut.exists()) {
                checkOut.createNewFile();
            }
            Utils.writeContents(checkOut, wantedVersion.getData());
        }
    }

    /**Checkout by the file name and commit ID.
     * @param commitID the CommitID string
     * @param fileName */
    public static void checkout(String commitID, String fileName)
            throws IOException {
        Commit desired = findCommitID(commitID);
        HashMap<String, Blob> desiredBlobs = desired.getBlobs();
        Blob wantedVersion = desiredBlobs.get(fileName);

        if (wantedVersion == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            File checkOut = new File(CWD, fileName);
            if (!checkOut.exists()) {
                checkOut.createNewFile();
            }
            Utils.writeContents(checkOut, wantedVersion.getData());
        }

    }

    /**Checkout by the branch name.
     * @param isBranch boolean expressing that param is a branch.
     * @param branch */
    public static void checkout(String branch, boolean isBranch)
            throws IOException {
        File branchFile = new File(Init.BRANCHES, branch);

        String currentBranch = Utils.readContentsAsString(Init.HEAD);
        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (branch.equals(currentBranch)) {
            System.out.println("No need to checkout the current branch.");
        }
        Commit oldHeadCommit = Commit.findPreviousCommit();
        HashMap<String, Blob> oldBlobs = oldHeadCommit.getBlobs();
        Utils.writeContents(Init.HEAD, branch);

        File headCommitFile  = new File(Init.BRANCHES, branch);
        String commitID = Utils.readContentsAsString(headCommitFile);
        Commit headCommit = Utils.readObject(
                new File(Init.COMMITS, commitID), Commit.class);
        String headCommitID = headCommit.getSHA1();
        HashMap<String, Blob> newBlobs = headCommit.getBlobs();

        for (HashMap.Entry<String, Blob> entry : newBlobs.entrySet()) {
            String currName = entry.getKey();
            File entryInCWD = new File(CWD, currName);
            if (!oldBlobs.containsKey(currName) && entryInCWD.exists()) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }
            checkout(headCommitID, currName);
        }

        for (HashMap.Entry<String, Blob> entry : oldBlobs.entrySet()) {
            String fileInOldBlob = entry.getKey();
            if (!newBlobs.containsKey(fileInOldBlob)) {
                Utils.restrictedDelete(fileInOldBlob);
            }
        }
        Commit.clearStagingArea();
    }
    /**Find's commit ID by the string, allowing for abbreviations.
     * @return Commit with that ID
     * @param commitID */
    public static Commit findCommitID(String commitID) {
        File desiredCommit = new File(Init.COMMITS, commitID);
        Commit ret = null;
        if (!desiredCommit.exists()) {
            for (String entry : Utils.plainFilenamesIn(Init.COMMITS)) {
                if (entry.contains(commitID)) {
                    return Utils.readObject(
                            new File(Init.COMMITS, entry), Commit.class);
                }
            }
            System.out.println("No commit with that id exists.");
            System.exit(0);
        } else {
            ret = Utils.readObject(desiredCommit, Commit.class);
        }
        return ret;
    }
}
