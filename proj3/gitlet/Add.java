package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Add {

    public void add(String fileName) throws IOException {
        if (!new File(fileName).exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        File removeFile = new File(Init.REMOVE_STAGE, fileName);
        if (removeFile.exists()) {
            // if in remove folder, take out of remove folder
            removeFile.delete();
        } else {
            Blob toAdd = new Blob(fileName);
            File addFile = new File(Init.ADD_STAGE, fileName);

            if (addFile.exists()) { //already exists: overwrite
                Utils.writeObject(addFile, toAdd);
            } else {
                //if it doesn't exist, compare to previous version.
                Commit last = Commit.findPreviousCommit();
                HashMap<String, Blob> blobMaps = last.getBlobs();
                Blob previousVersionBlob = blobMaps.get(fileName);

                if (previousVersionBlob == null) { //if blob doesn't exist in past commit
                    addFile.createNewFile();
                    Utils.writeObject(addFile, toAdd); //adds object to add stage
                } else {
                    Blob prev = Utils.readObject(new File(Init.BLOBS, fileName), Blob.class);
                    if (!prev.getData().equals(toAdd.getData())) {
                        addFile.createNewFile();
                        Utils.writeObject(addFile, toAdd); //adds object to add stage
                    } else {
                        if (addFile.exists()) {
                            Utils.restrictedDelete(addFile); //deletes from add stage if it's already there
                        }
                    }
                }



                /**if (previousVersionBlob == null || !previousVersionBlob.equals(toAdd)) {
                    addFile.createNewFile();
                    Utils.writeObject(addFile, toAdd); //adds object to add stage
                } else {
                    if (addFile.exists()) {
                        Utils.restrictedDelete(addFile); //deletes from add stage if it's already there
                    }
                } **/

            }
        }
    }



}
