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
        List<String> commands = Arrays.asList("init", "add");
        if (!commands.contains(args[0])) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        //fixme wrong number of operands?
        // not in initialized git directory error
        if (args[0] == "init") {
            init initial = new init();
            initial.init();
        } else if (args[0] == "add") {

        } else if (args[0] == "log") {

        }

        else {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

}
