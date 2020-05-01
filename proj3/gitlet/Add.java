package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**Add class for adding a file to the staging area.*/
public class Add {

    /**Add function for adding a file to the staging area.*/
    public void add(String fileName) throws IOException {
        if (!new File(fileName).exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        File removeFile = new File(Init.REMOVE_STAGE, fileName);
        if (removeFile.exists()) {
            removeFile.delete();
        } else {
            Blob toAdd = new Blob(fileName);
            File addFile = new File(Init.ADD_STAGE, fileName);

            if (addFile.exists()) {
                Utils.writeObject(addFile, toAdd);
            } else {
                Commit last = Commit.findPreviousCommit();
                HashMap<String, Blob> blobMaps = last.getBlobs();
                Blob previousVersionBlob = blobMaps.get(fileName);

                if (previousVersionBlob == null) {
                    addFile.createNewFile();
                    Utils.writeObject(addFile, toAdd);
                } else {
                    Blob prev = Utils.readObject(
                            new File(Init.BLOBS, fileName), Blob.class);
                    if (!prev.getData().equals(toAdd.getData())) {
                        addFile.createNewFile();
                        Utils.writeObject(addFile, toAdd);
                    } else {
                        if (addFile.exists()) {
                            Utils.restrictedDelete(addFile);
                        }
                    }
                }
            }
        }
    }



}
