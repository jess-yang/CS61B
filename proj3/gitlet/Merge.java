package gitlet;

import java.io.File;
import java.util.List;

public class Merge {
    public static void merge(String branch) {
        checkStageArea();//staged files present
        checkBranchValid(branch);//branch with name exists and isn't itself


        //fixme untracked files
    }

    public static void checkStageArea() {
        List addFileList = Utils.plainFilenamesIn(Init.ADD_STAGE);
        List removeFileList = Utils.plainFilenamesIn(Init.REMOVE_STAGE);
        if (addFileList.size() == 0 && removeFileList.size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
    }
    /**Checks that the branch name is valid and that the branch is not itself. */
    public static void checkBranchValid(String branch) {
        File branchFile = new File(Init.BRANCHES, branch);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (!branch.equals(Utils.readContentsAsString(Init.HEAD))) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }
}
