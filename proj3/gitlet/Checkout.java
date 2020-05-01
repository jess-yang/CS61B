package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Checkout {
    //fixme: abbreviated commit id shit?
    static final File CWD = new File(System.getProperty("user.dir"));

    public static void Checkout(String fileName) throws IOException {
        Commit last = Commit.findPreviousCommit();
        HashMap<String, Blob> lastBlobs = last.getBlobs();
        Blob wantedVersion = lastBlobs.get(fileName);
        // if file does not exist
        if (wantedVersion == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            //put in current directory
            String currentDirectory = System.getProperty("user.dir");
            File CWD = new File(currentDirectory);
            File checkOut = new File(CWD,fileName);
            if (!checkOut.exists()) { //if doesn't exist: make a file
                checkOut.createNewFile();
            }
            Utils.writeContents(checkOut, wantedVersion.getData());
        }
    }

    public static void Checkout(String commitID, String fileName) throws IOException {
        Commit desired = findCommitID(commitID);
        HashMap<String, Blob> desiredBlobs = desired.getBlobs();
        Blob wantedVersion = desiredBlobs.get(fileName);

        // if file does not exist
        if (wantedVersion == null) {
            //System.out.println("flag1"); //fixme
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            //put in current directory
            String currentDirectory = System.getProperty("user.dir");
            File CWD = new File(currentDirectory);
            File checkOut = new File(CWD,fileName);
            if (!checkOut.exists()) { //if doesn't exist: make a file
                checkOut.createNewFile();
            }
            Utils.writeContents(checkOut, wantedVersion.getData());
        }

    }

    public static void Checkout(String branch, boolean isBranch) throws IOException {
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

        Utils.writeContents(Init.HEAD, branch); // branch will now be current head


        File headCommitFile  = new File(Init.BRANCHES, branch);
        String commitID = Utils.readContentsAsString(headCommitFile);
        Commit headCommit = Utils.readObject(new File(Init.COMMITS, commitID), Commit.class);
        String headCommitID = headCommit.getSHA1();
        HashMap<String, Blob> newBlobs = headCommit.getBlobs(); //get files from head commit


        for (HashMap.Entry<String, Blob> entry : newBlobs.entrySet()){

            String currName = entry.getKey();
            File entryInCWD = new File(CWD, currName);
            if (!oldBlobs.containsKey(currName) && entryInCWD.exists()) {
                //check that checkout branch is NOT overwriting something that
                // 1. exists in current directory AND 2. is untracked.
                System.out.println("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
                System.exit(0);
            }

            Checkout(headCommitID, currName); //put files in working dir
        }

        //files tracked in current branch but not present in checked out are deleted
        for (HashMap.Entry<String, Blob> entry : oldBlobs.entrySet()){
            String fileInOldBlob = entry.getKey();
            if (!newBlobs.containsKey(fileInOldBlob)) {
                Utils.restrictedDelete(fileInOldBlob);
            }
        }

        Commit.clearStagingArea();  //staging area cleared

    }

    public static Commit findCommitID(String commitID) {
        File desiredCommit = new File(Init.COMMITS, commitID);
        Commit ret = null;
        if (!desiredCommit.exists()) { // check for wrong ID

            for (String entry : Utils.plainFilenamesIn(Init.COMMITS) ) {
                if (entry.contains(commitID)) {
                    return Utils.readObject(new File(Init.COMMITS, entry), Commit.class);
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
