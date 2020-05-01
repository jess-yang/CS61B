package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Reset {

    public static void reset(String commitID) throws IOException {
        File commitFile = new File(Init.COMMITS, commitID);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit oldHeadCommit = Commit.findPreviousCommit();
        HashMap<String, Blob> oldBlobs = oldHeadCommit.getBlobs();

        Commit thatCommit = Utils.readObject(commitFile, Commit.class);
        HashMap<String, Blob> commitBlobs = thatCommit.getBlobs();


        String thisBranch = Utils.readContentsAsString(Init.HEAD);
        Utils.writeContents(new File(Init.BRANCHES, thisBranch), commitID);


        for (HashMap.Entry<String, Blob> entry : oldBlobs.entrySet()) {
            String blobName = entry.getKey();
            if (!commitBlobs.containsKey(blobName)) {
                Utils.restrictedDelete(blobName);
            }
        }

        for (HashMap.Entry<String, Blob> entry : commitBlobs.entrySet()) {
            String blobName = entry.getKey();

            File entryInCWD = new File(Checkout.CWD, blobName);
            if (!oldBlobs.containsKey(blobName) && entryInCWD.exists()) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }

            Checkout.Checkout(commitID, blobName);
        }

        Commit.clearStagingArea();
    }
}
