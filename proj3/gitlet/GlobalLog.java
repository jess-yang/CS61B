package gitlet;

import java.io.File;

public class GlobalLog {

    public static void globalLog() {
        for (String branches : Utils.plainFilenamesIn(Init.BRANCHES)) {
            Commit head = Commit.findHeadCommit(branches);
            Log.log(head);

        }
    }
}
