package gitlet;

import java.io.File;
import java.io.IOException;

/** Brings down commits from the remote Gitlet repository
 * into the local Gitlet repository.
 * @author Jessica Yang*/
public class Fetch {

    /**Fetch method that takes in the name of the
     * remote directory and the remote branch name.
     * @param name name of remote dir
     * @param branchName name of remote branchname*/
    public static void fetch(String name, String branchName)
            throws IOException {
        File thisRemote = new File(AddRemote.REMOTES, name);
        String remotePath = Utils.readContentsAsString(thisRemote);
        File gitletDir = new File(remotePath);
        if (!gitletDir.exists()) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }

        File branchFile = new File(remotePath + "/branches", branchName);
        if (!branchFile.exists()) {
            System.out.println("That remote does not have that branch.");
            System.exit(0);
        }


        File localBranchDir = new File(Init.BRANCHES, name);
        if (!localBranchDir.exists()) {
            localBranchDir.mkdir();
        }
        File localNewBranch = new File(localBranchDir, branchName);

        if (!localNewBranch.exists()) {
            localNewBranch.createNewFile();
        }


        String remoteBranchHead = Utils.readContentsAsString(branchFile);
        File commitFile = new File(remotePath + "/commits", remoteBranchHead);
        Commit remoteBranchHeadCommit =
                Utils.readObject(commitFile, Commit.class);

        Utils.writeContents(localNewBranch, remoteBranchHeadCommit.getSHA1());


        while (remoteBranchHeadCommit != null) {
            File thisCommit = new File(Init.COMMITS,
                    remoteBranchHeadCommit.getSHA1());
            if (!thisCommit.exists()) {
                thisCommit.createNewFile();
            }
            Utils.writeObject(thisCommit, remoteBranchHeadCommit);

            remoteBranchHeadCommit = remoteBranchHeadCommit.getParent();
        }
    }
}
