package gitlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Jessica Yang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        } else if (!_commands.contains(args[0])) {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
        if (args[0].equals("init")) {
            Init initial = new Init();
            initial.init();
        }
        if (!Init.GITLET.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (args[0].equals("add") && args.length == 2) {
            Add add = new Add();
            add.add(args[1]);
        } else if (args[0].equals("log")) {
            Log.log();
        } else if (args[0].equals("commit")) {
            if (args.length == 2 && !args[1].equals("")) {
                Commit commit = new Commit(args[1]);
                commit.commitAction();
            } else {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
        } else if (args[0].equals("checkout")) {
            if (args.length == 2) {
                Checkout.checkout(args[1], true);
            } else if (args.length == 3 && args[1].equals("--")) {
                Checkout.checkout(args[2]);
            } else if (args.length == 4 && args[2].equals("--")) {
                Checkout.checkout(args[1], args[3]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        } else if (args[0].equals("rm") && args.length == 2) {
            Remove remove = new Remove();
            remove.remove(args[1]);
        } else if (args[0].equals("global-log") && args.length == 1) {
            GlobalLog gloLog = new GlobalLog();
            gloLog.globalLog();
        } else if (args[0].equals("find") && args.length == 2) {
            Find.find(args[1]);
        } else if (args[0].equals("status") && args.length == 1) {
            Status.status();
        } else if (args[0].equals("branch") && args.length == 2) {
            Branch.branch(args[1]);
        } else if (args[0].equals("rm-branch") && args.length == 2) {
            Remove.removeBranch(args[1]);
        } else if (args[0].equals("reset") && args.length == 2) {
            Reset.reset(args[1]);
        } else if (args[0].equals("merge") && args.length == 2) {
            Merge.merge(args[1]);
        }
    }

    /**List of commands for gitlet.*/
    private static List<String> _commands = Arrays.asList("init", "add", "log",
            "commit", "checkout", "rm", "global-log",
            "find", "status", "branch", "rm-branch", "reset", "merge");

}
