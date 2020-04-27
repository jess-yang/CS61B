package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Checkout {

    public void Checkout(String fileName) throws IOException {
        Commit last = Commit.findPreviousCommit();
        HashMap<String, Blob> lastBlobs = last.getBlob();
        Blob wantedVersion = lastBlobs.get(fileName);
        // if file does not exist
        if (wantedVersion == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            //put in current directory
            String currentDirectory = System.getProperty("user.dir");
            File CWD = new File(currentDirectory);
            File checkOut = new File(CWD,fileName);
            if (!checkOut.exists()) { //if doesn't exist: make a file
                checkOut.createNewFile();
            }
            Utils.writeContents(checkOut, wantedVersion.getData());
        }
    }

    public void Checkout(String commitID, String fileName) throws IOException {
        File desiredCommit = new File(Init.COMMITS, commitID);
        if (desiredCommit == null) { // check for wrong ID
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit desired = Utils.readObject(desiredCommit, Commit.class);
        HashMap<String, Blob> desiredBlobs = desired.getBlob();
        Blob wantedVersion = desiredBlobs.get(fileName);

        // if file does not exist
        if (wantedVersion == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            //put in current directory
            String currentDirectory = System.getProperty("user.dir");
            File CWD = new File(currentDirectory);
            File checkOut = new File(CWD,fileName);
            if (!checkOut.exists()) { //if doesn't exist: make a file
                checkOut.createNewFile();
            }
            Utils.writeContents(checkOut, wantedVersion.getData());
        }

    }

    public void Checkout(String branch, boolean isBranch) {

    }


}
