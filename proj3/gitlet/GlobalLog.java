package gitlet;

import java.io.File;

public class GlobalLog {

    public static void globalLog() {
        for (String commits : Utils.plainFilenamesIn(Init.COMMITS)) {
            Commit current = Utils.readObject(new File(Init.COMMITS, commits), Commit.class);
            Log.printOutput(current);
        }
    }
}
