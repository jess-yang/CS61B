package gitlet;

import java.io.File;

public class add {

    public void add(String fileName) {
        if (!new File(fileName).exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        //add to stage
        //compare to current version
    }

}
