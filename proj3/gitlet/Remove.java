package gitlet;

import java.io.File;
import java.io.IOException;

/** Remove class for rm function.
 *  @author Jessica Yang
 */
public class Remove {

    /**Method for removing a file by name.
     * @param name */
    public void remove(String name) throws IOException {
        File toRemoveFromAdd = new File(Init.ADD_STAGE, name);
        Commit last = Commit.findPreviousCommit();

        if (!toRemoveFromAdd.exists() && !last.getBlobs().containsKey(name)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        } else if (toRemoveFromAdd.exists()) {
            toRemoveFromAdd.delete();
        } else if (last.getBlobs().containsKey(name)) {
            Blob addTo = Utils.readObject(
                    new File(Init.BLOBS, name), Blob.class);
            File addToRemove = new File(Init.REMOVE_STAGE, name);
            addToRemove.createNewFile();
            Utils.writeObject(addToRemove, addTo);
            File fileName = new File(Checkout.CWD, name);
            if (fileName.exists()) {
                Utils.restrictedDelete(name);
            }
        }

    }
    /**Method for rm-branch, removing branch by name.
     * @param name */
    public static void removeBranch(String name) throws IOException {
        File branchToRemove = new File(Init.BRANCHES, name);
        if (!branchToRemove.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (Utils.readContentsAsString(Init.HEAD).equals(name)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            branchToRemove.delete();
        }
    }
}
