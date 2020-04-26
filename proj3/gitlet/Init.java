package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Init {
    public void init() {
        if (new File(".gitlet/").exists()) {
            System.out.println("A Gitlet version-control system " +
                    "already exists in the current directory.");
            System.exit(0);
        }


        new File(".gitlet/").mkdir();
        new File(".gitlet/branches").mkdir();
        new File(".gitlet/commits").mkdir();
        new File(".gitlet/stage").mkdir();

        String time = new SimpleDateFormat("hh:mm:ss z, EEE d MMM yyyy").format(new Date(0));
        String sha = Utils.sha1();



        //initial commit

    }
}
