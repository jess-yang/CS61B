package gitlet;


public class Log {

    //merge case????!!!

    /**prints the log of the history of commits of current active branch. */
    public static void log() {
        Commit recentest = Commit.findPreviousCommit();
        while (recentest != null) {
            printOutput(recentest);
            recentest = recentest.getParent();
        }

    }

    /**prints the log of the history of commits given the head. */
    public static void log(Commit head) {
        while (head != null) {
            printOutput(head);
            head = head.getParent();
        }
    }

    /**prints the output for one commit. */
    public static void printOutput(Commit current) {
        System.out.println("===");
        System.out.println("commit " + current.getSHA1());
        System.out.println("Date: " + current.getTime());
        System.out.println(current.getMessage());
        System.out.println();
    }
}
