package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import static enigma.EnigmaException.*;


/** Enigma simulator.
 *  @author Jessica Yang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */

    private void process() {
        Machine machine = readConfig();
        boolean checkSettings = false;
        while (_input.hasNextLine()) {
            String currentLine = _input.nextLine();
            if (!checkSettings) {
                if (!currentLine.contains("*")) {
                    throw error("no settings line");
                }
            }

            if (currentLine.contains("*")) {
                setUp(machine, currentLine);
            } else {
                String message = machine.convert(currentLine);
                printMessageLine(message);
            }
            checkSettings = true;
        }
    }


    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.next();
            _alphabet = new Alphabet(alphabet);
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            _allRotors = new ArrayList<Rotor>();

            _name = _config.next();
            while (_config.hasNext()) {
                _allRotors.add(readRotor(_name));
            }
            return new Machine(_alphabet, numRotors, numPawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }


    /** Return a rotor, reading its description from _config.
     * @param name */
    private Rotor readRotor(String name) {
        try {
            while (_config.hasNextLine()) {
                String notch = _config.next();
                Character type = notch.charAt(0);
                notch = notch.substring(1);

                String cycle = _config.next();
                String currName = _name;
                while (_config.hasNext()) {
                    boolean propClosed = false;
                    String check = _config.next();
                    if (check.charAt(0) == '(') {
                        if (check.indexOf(')') != -1) {
                            propClosed = true;
                        }
                        cycle += check;
                    } else {
                        _name = check;
                        break;
                    }

                    if (!propClosed) {
                        throw error("cycles not formatted right");
                    }
                }
                Permutation permutation = new Permutation(cycle, _alphabet);

                if (type.equals('M')) {
                    return new MovingRotor(currName, permutation, notch);
                } else if (type.equals('N')) {
                    return new FixedRotor(currName, permutation);
                } else if (type.equals('R')) {
                    return new Reflector(currName, permutation);
                }
            }
            throw error("no defined rotors");
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */

    private void setUp(Machine M, String settings) {
        int machineRotors = M.numRotors();

        String[] rotorArray = new String[M.numRotors()];
        Scanner scanner = new Scanner(settings);
        if (!scanner.next().equals("*")) {
            throw error("setting line not properly initiated ");
        }
        for (int i = 0; i < machineRotors && scanner.hasNext(); i++) {
            rotorArray[i] = scanner.next();
        }

        for (int i = 0; i < rotorArray.length; i++) {
            for (int j = i + 1; j < rotorArray.length; j++) {
                if (rotorArray[j].equals(rotorArray[i])) {
                    throw error("duplicate rotors");
                }
            }
        }
        M.insertRotors(rotorArray);
        if (!scanner.hasNext()) {
            throw error("expected and actual number of rotors didn't match");
        }
        String rotorSettings = scanner.next();

        M.setRotors(rotorSettings);


        String permutation = "";
        Boolean hasPlugboard = false;
        if (scanner.hasNext()) {
            hasPlugboard = true;
        }
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.charAt(0) == '(') {
                permutation += next;
            } else {
                break;
            }
        }
        if (hasPlugboard) {
            Permutation perms = new Permutation(permutation, _alphabet);
            M.setPlugboard(perms);
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String ret = "";
        char[] msgArray = msg.toCharArray();
        int index = 0;
        while (index < msg.length()) {
            for (int i = 0; i < 5 && index < msg.length(); i++) {
                ret += msgArray[index];
                index++;
            }
            ret += " ";
        }
        _output.println(ret);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Global variable name that is the name of rotor in _config.*/
    private String _name;

    /** collection of all the rotors in the configuration.**/
    private Collection<Rotor> _allRotors;
}
