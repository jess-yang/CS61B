package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Reset {
    //fixme: abbreviated ID shit
    public static void reset(String commitID) throws IOException {
        File commitFile = new File(Init.COMMITS, commitID);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
        }
        Commit oldHeadCommit = Commit.findPreviousCommit();
        HashMap<String, Blob> oldBlobs = oldHeadCommit.getBlob();

        Commit thatCommit = Utils.readObject(commitFile, Commit.class);
        HashMap<String, Blob> commitBlobs = thatCommit.getBlob();

        //branch head becomes this node
        String thisBranch = Utils.readContentsAsString(Init.HEAD);
        Utils.writeContents(new File(Init.BRANCHES, thisBranch), commitID );

        //remove tracked files not present in that commit
        for (HashMap.Entry<String, Blob> entry : oldBlobs.entrySet()){
            String blobName = entry.getKey();
            if (!commitBlobs.containsKey(blobName)) { //if old blob NOT in new blobs
                Utils.restrictedDelete(blobName);
            }

        }

        //getting all files in the desired commit's blob, and checks out each file individually
        for (HashMap.Entry<String, Blob> entry : commitBlobs.entrySet()){
            String blobName = entry.getKey();

            File entryInCWD = new File(Checkout.CWD, blobName);
            if (!oldBlobs.containsKey(blobName) && entryInCWD.exists()) { //overwrite error
                //check that checkout branch is NOT overwriting something that
                // 1. exists in current directory AND 2. is untracked.
                System.out.println("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
                System.exit(0);
            }

            Checkout.Checkout(commitID, blobName); //overwrite.
        }



        Commit.clearStagingArea();
    }
}
