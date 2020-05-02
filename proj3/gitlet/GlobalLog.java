package gitlet;

import java.io.File;

/** Class for globalLog, which prints out all commits.
 *  @author Jessica Yang
 */
public class GlobalLog {

    /**Global log method.*/
    public static void globalLog() {
        for (String commits : Utils.plainFilenamesIn(Init.COMMITS)) {
            Commit current = Utils.readObject(
                    new File(Init.COMMITS, commits), Commit.class);
            Log.printOutput(current);
        }
    }
}
