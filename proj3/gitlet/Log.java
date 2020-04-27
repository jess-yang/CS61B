package gitlet;


public class Log {

    public void log() {
        Commit recentest = Commit.findPreviousCommit();
        while (recentest != null) {
            printOutput(recentest);
            recentest = recentest.getParent();
        }

    }

    public void printOutput(Commit current) {
        System.out.println("===");
        System.out.println("commit " + current.getSHA1());
        System.out.println("Date: " + current.getTime());
        System.out.println(current.getMessage());
        System.out.println();
    }
}
