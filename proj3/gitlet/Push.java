package gitlet;

import java.io.File;
import java.io.IOException;

/**Attempts to append the current branch's commits to
 * the end of the given branch at the given remote.
 * @author Jessica Yang */
public class Push {
    /**Appends current branch commits to the branchName in
     * remote dir called Name.
     * @param name name of remote dir
     * @param branchName name of remote branch*/
    public static void pushRemote(String name, String branchName)
            throws IOException {
        File thisRemote = new File(AddRemote.REMOTES, name);
        String remotePath = Utils.readContentsAsString(thisRemote);
        File remoteGit = new File(remotePath);
        if (!remoteGit.exists()) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }

        File remoteBranchDir = new File(remotePath, "branches");
        if (!remoteBranchDir.exists()) {
            remoteBranchDir.mkdir();
        }
        File remoteBranchFile = new File(remoteBranchDir, branchName);
        if (!remoteBranchFile.exists()) {
            remoteBranchFile.createNewFile();
        }
        String remoteBranchHead = Utils.readContentsAsString(remoteBranchFile);
        File commitFile = new File(remotePath + "/commits", remoteBranchHead);
        Commit remoteBranchHeadCommit =
                Utils.readObject(commitFile, Commit.class);
        Commit current = Commit.findPreviousCommit();
        Commit matchPoint = null;
        while (current != null) {
            if (current.getSHA1().equals(remoteBranchHeadCommit.getSHA1())) {
                matchPoint = current;
                break;
            }
            current = current.getParent();
        }

        if (matchPoint == null) {
            System.out.println("Please pull down "
                    + "remote changes before pushing.");
            System.exit(0);
        }

        current = Commit.findPreviousCommit();
        while (!current.getSHA1().equals(matchPoint.getSHA1())) {
            File thisCommit = new File(remotePath
                    + "/commits", current.getSHA1());
            Utils.writeObject(thisCommit, current);
            current = current.getParent();

        }
        File matchPointFile = new File(remotePath
                + "/commits", matchPoint.getSHA1());
        Utils.writeObject(matchPointFile, matchPoint);


        current = Commit.findPreviousCommit();
        File headBranch = new File(remotePath + "/branches" + "/" + branchName);
        Utils.writeContents(headBranch, current.getSHA1());

    }
}
