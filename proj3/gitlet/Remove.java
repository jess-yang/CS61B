package gitlet;

import java.io.File;
import java.io.IOException;

public class Remove {

    public void remove(String name) throws IOException {
        File toRemoveFromAdd = new File(Init.ADD_STAGE, name);
        Commit last = Commit.findPreviousCommit();

        if (!toRemoveFromAdd.exists() && !last.getBlob().containsKey(name)) {
            //error if file not staged for add OR file not exist in the last commit's blob hashmap.
            System.out.println("No reason to remove the file.");
            System.exit(0);
        } else if (toRemoveFromAdd.exists()) {
            toRemoveFromAdd.delete();
        } else if(last.getBlob().containsKey(name)) {
            Blob addTo = Utils.readObject(new File(Init.BLOBS, name), Blob.class);
            File addToRemove = new File(Init.REMOVE_STAGE, name);
            addToRemove.createNewFile(); //fixme
            Utils.writeObject(addToRemove, addTo); //fixme
            File fileName = new File(Checkout.CWD, name); //check if user already deleted?
            if (fileName.exists()) { //check if user already deleted?
                Utils.restrictedDelete(name);
            }

        }

    }
    public static void removeBranch(String name) throws IOException {
        File branchToRemove = new File(Init.BRANCHES, name);
        if (!branchToRemove.exists()) { //name existence
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (Utils.readContentsAsString(Init.HEAD).equals(name)) {
            System.out.println("Cannot remove the current branch.");
        } else {        //deletes branch in branch folder

            branchToRemove.delete();
        }
    }
}
