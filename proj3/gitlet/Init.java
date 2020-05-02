package gitlet;
import java.io.File;
import java.io.IOException;


/**Initializes a git directory.
 * @author Jessica Yang*/
public class Init {
    /**File path for .gitlet folder.*/
    static final File GITLET = new File(".gitlet/");
    /**File path for HEAD file.*/
    static final File HEAD = new File(".gitlet/head");
    /**File path for BRANCHES folder.*/
    static final File BRANCHES = new File(".gitlet/branches");
    /**File path for COMMITS folder.*/
    static final File COMMITS = new File(".gitlet/commits");
    /**File path for BLOBS folder.*/
    static final File BLOBS = new File(".gitlet/blobs");
    /**File path for STAGE folder.*/
    static final File STAGE = new File(".gitlet/stage");
    /**File path for ADD folder.*/
    static final File ADD_STAGE = new File(".gitlet/stage/add");
    /**File path for REMOVE folder.*/
    static final File REMOVE_STAGE = new File(".gitlet/stage/remove");


    /**Init command.*/
    public void init() throws IOException {
        if (new File(".gitlet/").exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        }

        GITLET.mkdir();
        HEAD.createNewFile();
        BRANCHES.mkdir();
        COMMITS.mkdir();
        BLOBS.mkdir();
        STAGE.mkdir();
        ADD_STAGE.mkdir();
        REMOVE_STAGE.mkdir();

        String branch = "master";
        Utils.writeContents(HEAD, branch);

        File branchFile = new File(BRANCHES + branch);
        branchFile.createNewFile();

        Commit initial = new Commit();
        Utils.writeContents(branchFile, initial.getSHA1());
        initial.firstCommitAction();
    }



}
