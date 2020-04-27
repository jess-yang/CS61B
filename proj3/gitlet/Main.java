package gitlet;

import java.util.Arrays;
import java.util.List;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Jessica Yang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // FILL THIS IN
        List<String> commands = Arrays.asList("init", "add", "log", "commit"); //fixme; add commands
        if (!commands.contains(args[0])) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        // fixme not in initialized git directory error
        if (args[0] == "init") {
            Init initial = new Init();
            initial.init();
        } else if (args[0] == "add") {
            if (checkArgs(args, 2)) {
                Add add = new Add();
                add.add(args[1]);
            } else {
                System.out.println("Please enter a file to add."); //fixme: not correct terms?
            }
        } else if (args[0] == "log") {

        } else if (args[0] == "commit") {
            if (checkArgs(args, 2)) {
                //Commit commit = new Commit();
                //commit.commit(args[1]);
            } else {
                System.out.println("Please enter a commit message.");
            }
        }

        else {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

    public static boolean checkArgs(String[] args, int expected) {
        return args.length == expected;
    }

}
