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
        List<String> commands = Arrays.asList("init", "add", "log", "commit", "checkout", "rm", "global-log",
                "find", "status", "branch", "rm-branch", "reset"); //fixme; add commands
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }else if (!commands.contains(args[0])) {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
        // fixme: not in initialized git directory error-- check for .gitlet folder?
        if (args[0].equals("init")) {
            Init initial = new Init();
            initial.init();
        } else if (args[0].equals("add")) {
            if (args.length == 2) {
                Add add = new Add();
                add.add(args[1]);
            }
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
                Checkout.Checkout(args[1], true);
            } else if (args.length == 3 && args[1].equals("--")) {
                Checkout.Checkout(args[2]);
            } else if (args.length == 4 && args[2].equals("--")) {
                Checkout.Checkout(args[1], args[3]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
        } else if (args[0].equals("rm")) {
            if (args.length == 2) {
                Remove remove = new Remove();
                remove.remove(args[1]);
            }

        } else if (args[0].equals("global-log")) {
            if (args.length == 1) {
                GlobalLog gloLog = new GlobalLog();
                gloLog.globalLog();
            }
        } else if (args[0].equals("find")) {
            if (args.length == 2) {
                Find.find(args[1]);
            }
        } else if (args[0].equals("status")) {
            if (args.length == 1) {
                Status.status();
            }
        } else if (args[0].equals("branch")) {
            if (args.length == 2) {
                Branch.branch(args[1]);
            }
        } else if (args[0].equals("rm-branch") && args.length == 2) {
            Remove.removeBranch(args[1]);
        } else if (args[0].equals("reset") && args.length == 2) {
            Reset.reset(args[1]);
        }
    }

}
