package gitlet;

import java.io.IOException;

/**Pull class.
 * Fetches branch [remote name]/[remote branch name] as for the fetch command,
 * and then merges that fetch into the current branch.
 * @author Jessica Yang*/
public class Pull {

    /**Pulls the branch in remote directory Name.
     * @param name name of remote dir
     * @param branchName name of remote branch*/
    public static void pull(String name, String branchName) throws IOException {
        Fetch.fetch(name, branchName);
        Merge.merge(name + "/" + branchName);
    }
}
