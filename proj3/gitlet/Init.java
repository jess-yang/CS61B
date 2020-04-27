package gitlet;


import java.io.File;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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





        //initial commit

    }
}
