package gitlet;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class Init {

    public void init() throws IOException {
        if (new File(".gitlet/").exists()) {
            System.out.println("A Gitlet version-control system " +
                    "already exists in the current directory.");
            System.exit(0);
        }

        _gitlet = new File(".gitlet/");
        _gitlet.mkdir();

        new File(".gitlet/head").createNewFile();
        new File(".gitlet/branches").mkdir();
        new File(".gitlet/commits").mkdir();
        new File(".gitlet/blobs").mkdir();
        new File(".gitlet/stage").mkdir();
        _stageAdd = new File(".gitlet/stage/add");
        _stageAdd.mkdir();
        //new File(".gitlet/stage/add/addFile").createNewFile();
        new File(".gitlet/stage/remove").mkdir();
        //new File(".gitlet/stage/remove/removeFile").createNewFile();



        //initial commit

        String time = new SimpleDateFormat("hh:mm:ss z, EEE d MMM yyyy").format(new Date());
        HashMap<String, Blob> blob = new HashMap<String,Blob>();

        Commit initial = new Commit(time, "initial message", null, blob); //new commit object
        initial.commitAction();

        //branch

        //write headfile
    }

    public File getAddStage() {
        return _stageAdd;
    }

    public File _gitlet;
    public File _branches;
    public File _stageAdd;
}
