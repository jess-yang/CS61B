package gitlet;

public class Status {
    public static void status() {
        System.out.println("=== Branches ===");
        for (String branches : Utils.plainFilenamesIn(Init.BRANCHES)) {
            if (branches.equals(Utils.readContentsAsString(Init.HEAD))) {
                System.out.print("*");
            }
            System.out.println(branches);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String filesAdd : Utils.plainFilenamesIn(Init.ADD_STAGE)) {
            System.out.println(filesAdd);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String filesRem : Utils.plainFilenamesIn(Init.REMOVE_STAGE)) {
            System.out.println(filesRem);
        }
        System.out.println();

        //fixme: EC
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
    }
}
