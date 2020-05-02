package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/** Merge class.
 *  @author Jessica Yang
 */
public class Merge {
    /** Merge action.
     * @param branch */

    public static void merge(String branch) throws IOException {
        checkStageArea();
        checkBranchValid(branch);
        String currBranch = Utils.readContentsAsString(Init.HEAD);
        File branchFile = new File(Init.BRANCHES, branch);
        Commit currHead = Commit.findHeadCommit(currBranch);
        Commit branchHead = Commit.findHeadCommit(branch);
        HashMap<String, Blob> currBlobs = currHead.getBlobs();
        HashMap<String, Blob> branchBlobs = branchHead.getBlobs();

        Commit splitPoint = findSplitPoint(branchHead);
        HashMap<String, Blob> splitBlobs = splitPoint.getBlobs();

        if (splitPoint.getSHA1().equals(branchHead.getSHA1())) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            System.exit(0);
        } else if (splitPoint.getSHA1().equals(currHead.getSHA1())) {
            Checkout.checkout(branch, true);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        blobIterate(currBlobs, branchBlobs, splitBlobs, branchHead);
        Commit mergeCommit = new Commit(currHead, branchHead,
                "Merged " + branch + " into " + currBranch + ".");
        mergeCommit.commitAction();
        if (isMergeConflict) {
            System.out.println("Encountered a merge conflict.");

        }

    }
    /**Main function of merge; iterates through the blobs
     * and chooses what action to perform.
     * @param branchHead Head commit of given branch
     * @param branchBlobs files tracked in blob
     * @param currBlobs files tracked in active head
     * @param splitBlobs files tracked at split point*/
    public static void blobIterate(HashMap<String, Blob> currBlobs,
                                   HashMap<String, Blob> branchBlobs,
                                   HashMap<String, Blob> splitBlobs,
                                   Commit branchHead) throws IOException {
        for (HashMap.Entry<String, Blob> branchEntry : branchBlobs.entrySet()) {
            Blob currentBlob = currBlobs.get(branchEntry.getKey());
            Blob branchBlob = branchEntry.getValue();
            Blob splitBlob = splitBlobs.get(branchEntry.getKey());
            if (splitBlob == null) {
                if (currentBlob == null) {
                    checkOverwrite(currBlobs, branchEntry.getKey());
                    Checkout.checkout(branchHead.getSHA1(),
                            branchEntry.getKey());
                    Utils.writeObject(new File(Init.ADD_STAGE,
                            branchEntry.getKey()), branchEntry.getValue());
                } else if (modified(branchBlob, currentBlob)) {
                    isMergeConflict = true;
                    mergeConflictWrite(currentBlob, branchBlob);
                    Utils.writeObject(new File(Init.ADD_STAGE,
                            currentBlob.getName()), merged);
                }
            } else if (currentBlob == null) {
                if (modified(splitBlob, branchBlob)) {
                    isMergeConflict = true;
                    mergeConflictWrite(currentBlob, branchBlob);
                    Utils.writeObject(new File(Init.REMOVE_STAGE,
                            branchBlob.getName()), merged);
                }
            } else if (modified(splitBlob, branchBlob)
                    && !modified(splitBlob, currentBlob)) {
                checkOverwrite(currBlobs, branchEntry.getKey());
                Checkout.checkout(branchHead.getSHA1(), branchEntry.getKey());
                Utils.writeObject(new File(Init.ADD_STAGE,
                        branchEntry.getKey()), branchEntry.getValue());
            } else if (modified(splitBlob, branchBlob)
                    && modified(splitBlob, currentBlob)) {
                if (modified(branchBlob, currentBlob)) {
                    isMergeConflict = true;
                    mergeConflictWrite(currentBlob, branchBlob);
                    Utils.writeObject(new File(Init.ADD_STAGE,
                            currentBlob.getName()), merged);
                }
            }
        }
        for (HashMap.Entry<String, Blob> splitEntry : splitBlobs.entrySet()) {
            Blob currentBlob = currBlobs.get(splitEntry.getKey());
            Blob branchBlob = branchBlobs.get(splitEntry.getKey());
            Blob splitBlob = splitEntry.getValue();
            if (branchBlob == null) {
                if (!modified(splitBlob, currentBlob)) {
                    checkOverwrite(currBlobs, splitBlob.getName());
                    Utils.writeObject(new File(Init.REMOVE_STAGE,
                            splitBlob.getName()), splitBlob);
                    Utils.restrictedDelete(splitBlob.getName());
                } else {
                    isMergeConflict = true;
                    mergeConflictWrite(currentBlob, branchBlob);
                    Utils.writeObject(new File(Init.ADD_STAGE,
                            currentBlob.getName()), merged);
                }
            }
        }
    }


    /**Rewrites files due to merge conflict.
     * @param branch Blob file from branch
     * @param current Blob file in the current active branch*/
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


    /**Checks that the branch name is valid and that the branch is not itself.
     * @param branch name of branch*/
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

    /**Finds the split point.
     * @return commit of splitpoint
     * @param branchhead the commit at the head of the branch*/
    public static Commit findSplitPoint(Commit branchhead) {
        String currBranch = Utils.readContentsAsString(Init.HEAD);
        Commit currCommit = Commit.findHeadCommit(currBranch);

        LinkedList<String> branches = new LinkedList<String>();
        while (branchhead != null) {
            branches.add(branchhead.getSHA1());
            if (branchhead.getParent2() != null) {
                branches.add(branchhead.getParent2().getSHA1());
            }
            branchhead = branchhead.getParent();
        }

        LinkedList<Commit> queue = new LinkedList<Commit>();
        queue.add(currCommit);

        while (queue.size() != 0) {
            Commit check = queue.poll();
            if (branches.contains(check.getSHA1())) {
                return check;
            }
            LinkedList<Commit> adjacent = new LinkedList<Commit>();
            if (check.getParent2() != null) {
                adjacent.add(currCommit.getParent2());
                queue.remove(check);
                queue.add(check.getParent2());
            }
            if (check.getParent() != null) {
                adjacent.add(currCommit.getParent());
                queue.remove(check);
                queue.add(check.getParent());
            }

        }
        return null;
    }

    /**Returns whether or not the contents in two blobs are equal.
     * @param current current file
     * @param branch file in branches*/
    public static boolean modified(Blob current, Blob branch) {
        if (!current.getData().equals(branch.getData())) {
            return true;
        }
        return false;

    }

    /**Checks if the merge action will overwrite untracked files.
     * @param name of file
     * @param blobs HashMap of current files tracked*/
    public static void checkOverwrite(HashMap<String, Blob> blobs,
                                      String name) {
        File entryInCWD = new File(Checkout.CWD, name);
        if (!blobs.containsKey(name) && entryInCWD.exists()) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        }
    }

    /**Instance variable for the new blobs after merge.*/
    private static Blob merged;
    /**Tracks if the merge call is a conflict.*/
    private static boolean isMergeConflict = false;
}
