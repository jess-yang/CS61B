package gitlet;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class Init {

    static final File GITLET = new File(".gitlet/");
    static final File HEAD = new File(".gitlet/head");
    static final File BRANCHES = new File(".gitlet/branches");
    static final File COMMITS = new File(".gitlet/commits");
    static final File BLOBS = new File(".gitlet/blobs");
    static final File STAGE = new File(".gitlet/stage");
    static final File ADD_STAGE = new File(".gitlet/stage/add");
    static final File REMOVE_STAGE = new File(".gitlet/stage/remove");

    public void init() throws IOException {
        if (new File(".gitlet/").exists()) {
            System.out.println("A Gitlet version-control system " +
                    "already exists in the current directory.");
            System.exit(0);
        }

        //new File(".gitlet/");
        GITLET.mkdir();
        //new File(".gitlet/head")
        HEAD.createNewFile();
        //new File(".gitlet/branches")
        BRANCHES.mkdir();
        //new File(".gitlet/commits")
        COMMITS.mkdir();
        //new File(".gitlet/blobs")
        BLOBS.mkdir(); //fixme : ???
        //new File(".gitlet/stage")
        STAGE.mkdir();
        //new File(".gitlet/stage/add")
        ADD_STAGE.mkdir();
        //new File(".gitlet/stage/remove")
        REMOVE_STAGE.mkdir();

        //head file
        String branch = "master";
        //Utils.writeContents(new File(".gitlet/head"), branch);
        Utils.writeContents(HEAD, branch);

        //branch
        //File branchFile = new File(".gitlet/branches/" + branch);
        File branchFile = new File(BRANCHES + branch);
        branchFile.createNewFile();

        //initial commit

        String time = new SimpleDateFormat("hh:mm:ss z, EEE d MMM yyyy").format(new Date());
        HashMap<String, Blob> blob = new HashMap<String,Blob>();

        Commit initial = new Commit(); //new commit object


        Utils.writeContents(branchFile, initial.getSHA1());

        initial.firstCommitAction(); //fixme changed methods

    }



}
