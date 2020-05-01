package gitlet;

import java.io.File;
import java.io.IOException;

/** Branch class.*/
public class Branch {

    /** Creates a new branch and adds it to the .gitlet folder.*/
    public static void branch(String branchName) throws IOException {
        String currentBranch = Utils.readContentsAsString(Init.HEAD);
        String headSHA1 = Utils.readContentsAsString(
                new File(Init.BRANCHES, currentBranch));
        File current = new File(Init.BRANCHES, branchName);
        if (current.exists()) {
            System.out.println("A branch with that name already exists.");
        } else {
            current.createNewFile();
            Utils.writeContents(current, headSHA1);
        }
    }
}
