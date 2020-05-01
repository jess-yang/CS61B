package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Merge {

    public static void merge(String branch) throws IOException {
        checkStageArea();//staged files present
        checkBranchValid(branch);//branch with name exists and isn't itself
        String currBranch = Utils.readContentsAsString(Init.HEAD);
        File branchFile = new File(Init.BRANCHES, branch);
        Commit currHead = Commit.findHeadCommit(currBranch);
        Commit branchHead = Commit.findHeadCommit(branch);
        HashMap<String, Blob> currBlobs = currHead.getBlobs();
        HashMap<String, Blob> branchBlobs = branchHead.getBlobs();

        boolean isMerge = false;

        Commit splitPoint = findSplitPoint(branch);
        HashMap<String, Blob> splitBlobs = splitPoint.getBlobs();

        if (splitPoint.getSHA1().equals(branchHead.getSHA1())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if(splitPoint.getSHA1().equals(currHead.getSHA1())) {
            Checkout.Checkout(branch, true);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        for (HashMap.Entry<String, Blob> branchEntry : branchBlobs.entrySet()){
            Blob currentBlob = currBlobs.get(branchEntry.getKey());
            Blob branchBlob = branchEntry.getValue();
            Blob splitBlob = splitBlobs.get(branchEntry.getKey());
            if (splitBlob == null) {
                if (currentBlob == null) {//case5
                    checkOverwrite(currBlobs, branchEntry.getKey());
                    Checkout.Checkout(branchHead.getSHA1(), branchEntry.getKey());
                    Utils.writeObject(new File(Init.ADD_STAGE, branchEntry.getKey()), branchEntry.getValue());
                } else if (modified(branchBlob, currentBlob)) {
                    //fixme scenario3
                    System.out.println("flag1"); //fixme
                    isMerge = true;
                    mergeConflictWrite(currentBlob, branchBlob);
                    Utils.writeObject(new File(Init.ADD_STAGE, currentBlob.getName()), currentBlob);
                }
            } else if (currentBlob == null) {
                if (modified(splitBlob, branchBlob)) {
                    //fixme scenario 2
                    isMerge = true;
                    mergeConflictWrite(currentBlob, branchBlob);
                    Utils.writeObject(new File(Init.REMOVE_STAGE, branchBlob.getName()), merged);
                }
            }
            else if(modified(splitBlob, branchBlob) && !modified(splitBlob, currentBlob)) { //case1
                checkOverwrite(currBlobs, branchEntry.getKey());
                Checkout.Checkout(branchHead.getSHA1(), branchEntry.getKey());
                Utils.writeObject(new File(Init.ADD_STAGE, branchEntry.getKey()), branchEntry.getValue());
            } else if (modified(splitBlob, branchBlob) && modified(splitBlob, currentBlob)) {
                //fixme: scenario1 conflict
                if (modified(branchBlob, currentBlob)) {
                    //System.out.println("flag3"); //fixme
                    isMerge = true;
                    mergeConflictWrite(currentBlob, branchBlob);
                    Utils.writeObject(new File(Init.ADD_STAGE, currentBlob.getName()), currentBlob);
                }

            }
        }
        for (HashMap.Entry<String, Blob> splitEntry : splitBlobs.entrySet()){
            Blob currentBlob = currBlobs.get(splitEntry.getKey());
            Blob branchBlob = branchBlobs.get(splitEntry.getKey());
            Blob splitBlob = splitEntry.getValue();
            if (currentBlob == null) {
                if (branchBlob != null) {
                    //if(!modified(splitBlob, branchBlob) && currentBlob == null) { //case7
                }
            } else if (branchBlob == null) {
                if (!modified(splitBlob, currentBlob)) { //case6
                    checkOverwrite(currBlobs, splitBlob.getName());
                    Utils.writeObject(new File(Init.REMOVE_STAGE, splitBlob.getName()), splitBlob);
                    Utils.restrictedDelete(splitBlob.getName());
                } else {
                    //fixme scenario2
                    System.out.println("flag4"); //fixme
                    isMerge = true;
                    mergeConflictWrite(currentBlob, branchBlob);
                    Utils.writeObject(new File(Init.ADD_STAGE, currentBlob.getName()), currentBlob);
                }
            }
        }
        if (!isMerge) {
            Commit mergeCommit = new Commit("Merged "+ branch +" into "+currBranch+".");
            mergeCommit.commitAction();
        } else {
            //fixme: merge conflict commit
            Commit mergeCommit = new Commit(currHead, branchHead,
                    "Merged "+ branch +" into "+currBranch+".");
            mergeCommit.commitAction();
            System.out.println("Encountered a merge conflict.");
        }

    }

    /**Rewrites files due to merge conflict. */
    public static void mergeConflictWrite(Blob current, Blob branch) {
        String firstHead = "<<<<<<< HEAD\n";
        String middle = "=======\n";
        String close = ">>>>>>>\n";
        String fileName = null;
        String currentData = "";
        String branchData = "";
        if (current != null && branch != null) {
            fileName = current.getName();
        }
        if (current == null) {
            fileName = branch.getName();
        } else {
            currentData = current.getData();
        }
        if (branch == null) {
            fileName = current.getName();
        } else {
           branchData = branch.getData();
        }
        String message = firstHead + currentData
                + middle + branchData + close;

        File file = new File(Checkout.CWD, fileName);
        Utils.writeContents(file, message);
        merged = new Blob(fileName);
    }



    /**Checks to make sure stage area is cleared before attempting merge. */
    public static void checkStageArea() {
        List addFileList = Utils.plainFilenamesIn(Init.ADD_STAGE);
        List removeFileList = Utils.plainFilenamesIn(Init.REMOVE_STAGE);
        if (addFileList.size() != 0 || removeFileList.size() != 0) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
    }


    /**Checks that the branch name is valid and that the branch is not itself. */
    public static void checkBranchValid(String branch) {
        File branchFile = new File(Init.BRANCHES, branch);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (branch.equals(Utils.readContentsAsString(Init.HEAD))) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
    }
    /**Finds the split point of a branch and the current active branch. */
    public static Commit findSplitPoint(String branch) {
        String currBranch = Utils.readContentsAsString(Init.HEAD);
        Commit currCommit = Commit.findHeadCommit(currBranch);
        Commit branchCommit = Commit.findHeadCommit(branch);

        while (currCommit != null) {
            while (branchCommit != null) {
                if (currCommit.getSHA1().equals(branchCommit.getSHA1())) {
                    return currCommit;
                }
                branchCommit = branchCommit.getParent();
            }
            branchCommit = Commit.findHeadCommit(branch);
            currCommit = currCommit.getParent();
        }
        return null;
    }

    /**Returns whether or not the contents in two blobs are equal. */
    public static boolean modified(Blob current, Blob branch) {
        if (!current.getData().equals(branch.getData())) {
            return true;
        } else {
            return false;
        }
    }

    public static void checkOverwrite(HashMap<String, Blob> blobs, String name) {
        File entryInCWD = new File(Checkout.CWD, name);
        if (!blobs.containsKey(name) && entryInCWD.exists()) {
            //check that checkout branch is NOT overwriting something that
            // 1. exists in current directory AND 2. is untracked.
            System.out.println("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
            System.exit(0);
        }
    }

    private static Blob merged;
}
