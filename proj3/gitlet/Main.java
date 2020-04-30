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
        List<String> commands = Arrays.asList("init", "add", "log", "commit", "checkout"); //fixme; add commands
        if (!commands.contains(args[0])) {
            System.out.println("Please enter a command.");
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
            } else {
                System.out.println("Please enter a file to add."); //fixme: not correct terms?
            }
        } else if (args[0].equals("log")) {
            //Log log = new Log();
            //log.log();
            Log.log();

        } else if (args[0].equals("commit")) {
            if (args.length == 2) {
                Commit commit = new Commit(args[1]);
                commit.commitAction();
            } else {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
        } else if (args[0].equals("checkout")) {
            if (args.length == 2) {
                Checkout checkout = new Checkout();
                checkout.Checkout(args[1], true);
            } else if (args.length == 3) {
                Checkout checkout = new Checkout();
                checkout.Checkout(args[2]);

            } else if (args.length == 4) {
                Checkout checkout = new Checkout();
                checkout.Checkout(args[1], args[3]);
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

        }

        else {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

}
