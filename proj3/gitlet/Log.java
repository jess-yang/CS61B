package gitlet;

/**Log class that prints the history of commits.
 * @author Jessica Yang */
public class Log {

    /**prints the log of the history of commits of current active branch. */
    public static void log() {
        Commit recentest = Commit.findPreviousCommit();
        while (recentest != null) {
            printOutput(recentest);
            recentest = recentest.getParent();
        }

    }

    /**prints the log of the history of commits given the head.
     * @param head */
    public static void log(Commit head) {
        while (head != null) {
            printOutput(head);
            head = head.getParent();
        }
    }

    /**prints the output for one commit.
     * @param current */
    public static void printOutput(Commit current) {
        System.out.println("===");
        System.out.println("commit " + current.getSHA1());
        System.out.println("Date: " + current.getTime());
        System.out.println(current.getMessage());
        System.out.println();
    }
}
