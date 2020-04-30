package gitlet;

import java.io.File;

public class Find {
    public static void find(String message) {
        //iterates through all commits and checks for message equality
        boolean found = false;
        for (String commits : Utils.plainFilenamesIn(Init.COMMITS)) {
            File commitFile = new File(Init.COMMITS, commits);
            Commit current = Utils.readObject(commitFile, Commit.class);
            if (current.getMessage().equals(message)) {
                found = true;
                System.out.println(current.getSHA1());
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }
}
