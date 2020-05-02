package gitlet;

import java.io.File;
import java.util.HashMap;

/**Status class that displays the status of gitlet.*/
public class Status {

    /**Status method that displays the status of gitlet.*/
    public static void status() {
        System.out.println("=== Branches ===");
        for (String branches : Utils.plainFilenamesIn(Init.BRANCHES)) {
            if (branches.equals(Utils.readContentsAsString(Init.HEAD))) {
                System.out.print("*");
            }
            System.out.println(branches);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String filesAdd : Utils.plainFilenamesIn(Init.ADD_STAGE)) {
            System.out.println(filesAdd);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String filesRem : Utils.plainFilenamesIn(Init.REMOVE_STAGE)) {
            System.out.println(filesRem);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        Commit current = Commit.findPreviousCommit();
        HashMap<String, Blob> blobs = current.getBlobs();
        for (HashMap.Entry<String, Blob> entry : blobs.entrySet()){
            File CWDFile = new File(Checkout.CWD, entry.getKey());

            File inAdd = new File(Init.ADD_STAGE, entry.getKey());
            File inRemoval = new File(Init.REMOVE_STAGE, entry.getKey());
            if(!CWDFile.exists() && !inRemoval.exists()) {
                System.out.println(entry.getKey() + " (deleted)");
            } else if (CWDFile.exists()){
                String contents = Utils.readContentsAsString(CWDFile);
                String inAddContents = entry.getValue().getData();
                if (!inAdd.exists() && !contents.equals(inAddContents)) {
                    System.out.println(entry.getKey() + " (modified)");
                }
            } else if (!CWDFile.exists() && inAdd.exists()) {
                System.out.println(entry.getKey() + " (deleted)");
            }
        }
        for (String fileName : Utils.plainFilenamesIn(Init.ADD_STAGE)) {
            File CWDFile = new File(Checkout.CWD, fileName);
            File inAddFile = new File(Init.ADD_STAGE, fileName);
            String CWDContents = Utils.readContentsAsString(CWDFile);
            String addContents = Utils.readObject(inAddFile, Blob.class).getData();
            if (CWDFile.exists() && !CWDContents.equals(addContents)) {
                System.out.println(fileName + " (modified)");
            }
        }

        System.out.println();

        System.out.println("=== Untracked Files ===");
        for(String fileName : Utils.plainFilenamesIn(Checkout.CWD)) {
            File inAdd = new File(Init.ADD_STAGE, fileName);
            File inRemove = new File(Init.REMOVE_STAGE, fileName);
            Boolean inCommits = current.getBlobs().containsKey(fileName);
            if (!inAdd.exists() && !inCommits && !inRemove.exists()) {
                System.out.println(fileName);
            }
        }
        System.exit(0);
    }
}
