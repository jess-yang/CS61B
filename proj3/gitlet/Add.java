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
        File addFile = new File(Init.ADD_STAGE, fileName);

        File removeFile = new File(Init.REMOVE_STAGE, fileName);
        if (removeFile.exists()) {
            // if in remove folder, take out of remove folder
            Utils.restrictedDelete(removeFile);
        } //else {
            if (addFile.exists()) {
                //already exists: overwrite

                Utils.writeObject(addFile, toAdd);

            } else {
                //if it doesn't exist, compare to previous version.

                Commit last = Commit.findPreviousCommit();
                HashMap<String, Blob> blobMaps = last.getBlob();
                Blob previousVersionBlob = blobMaps.get(fileName);

                //new below
                if (previousVersionBlob == null ) {
                    addFile.createNewFile();
                    Utils.writeObject(addFile, toAdd); //adds object to add stage
                } else {
                    byte[] toAddData = toAdd.getData(); //fixme new
                    byte[] previousData = previousVersionBlob.getData(); //fixme new
                    if (!previousData.equals(toAddData)) {
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
        //}
    }



}
