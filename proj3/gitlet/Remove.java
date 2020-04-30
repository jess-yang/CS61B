package gitlet;

import java.io.File;
import java.io.IOException;

public class Remove {

    public void remove(String name) throws IOException {
        File toRemoveFromAdd = new File(Init.ADD_STAGE, name);
        Commit last = Commit.findPreviousCommit();

        if (!toRemoveFromAdd.exists() || last.getBlob().get(name) == null) {
            //error if file not staged for add OR file not exist in the last commit's blob hashmap.
            System.out.println("No reason to remove the file.");
            System.exit(0);
        } else if (toRemoveFromAdd.exists()) {
            toRemoveFromAdd.delete();
        } else if(last.getBlob().get(name) != null) {
            Blob addTo = new Blob(name);
            File addToRemove = new File(Init.REMOVE_STAGE, name);
            addToRemove.createNewFile();
            Utils.writeObject(addToRemove, addTo);
            Utils.restrictedDelete(name); // removes from CWD
        }


        //actual removal is done in commit.
    }


}
