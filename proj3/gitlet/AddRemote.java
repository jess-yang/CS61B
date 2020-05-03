package gitlet;

import java.io.File;
import java.io.IOException;

/**Add remotes class.
 * @author Jessica Yang*/
public class AddRemote {

    /**File path of remotes folder in LOCAL gitlet.*/
    static final File REMOTES = new File(".gitlet/remotes");

    /**Adds a pointer to the remote directory in the local gitlet.
     * Saves it in REMOTES folder, with file name as name and contents as
     * dirName.
     * @param name name of remote directory
     * @param dirName file path of directory*/
    public static void addRemote(String name, String dirName)
            throws IOException {
        File remoteDir = new File(REMOTES, name);
        if (remoteDir.exists()) {
            System.out.println("A remote with that name already exists.");
            System.exit(0);
        } else {
            if (!REMOTES.exists()) {
                REMOTES.mkdir();
            }
            File remoteFile = new File(REMOTES, name);
            remoteFile.createNewFile();
            Utils.writeContents(remoteFile, dirName);
        }
    }
}
