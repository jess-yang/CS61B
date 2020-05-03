package gitlet;

import java.io.File;

/**Removes the pointer to this remote dir in LOCAL.
 * @author Jessica Yang */
public class RemoveRemote {
    /**Removes the remote.
     * @param name of remote dir*/
    public static void removeRemote(String name) {
        File remoteFile = new File(AddRemote.REMOTES, name);
        if (!remoteFile.exists()) {
            System.out.println("A remote with that name does not exist.");
            System.exit(0);
        } else {
            remoteFile.delete();
        }
    }
}
