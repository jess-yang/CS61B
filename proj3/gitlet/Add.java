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

        Blob toAdd = new Blob(fileName);
        File addFile = new File(".gitlet/stage/add/" + fileName);

        File removeFile = new File(".gitlet/stage/remove/" +fileName);
        if (removeFile.exists()) {
            // if in remove folder, take out of remove folder
            Utils.restrictedDelete(removeFile);
        } else {
            if (addFile.exists()) {
                //already exists: overwrite
                Utils.writeObject(addFile, toAdd);

            } else {
                //if it doesn't exist, compare to previous version.

                Commit last = Commit.findPreviousCommit();
                HashMap<String, Blob> blobMaps = last.getBlob();
                Blob previousVersionBlob = blobMaps.get(fileName);

                if (previousVersionBlob == null || !previousVersionBlob.equals(toAdd)) {
                    addFile.createNewFile();
                    Utils.writeObject(addFile, toAdd); //adds object to add stage
                } else {
                    if (addFile.exists()) {
                        Utils.restrictedDelete(addFile); //deletes from add stage if it's already there
                    }
                }

            }
        }
    }



}
