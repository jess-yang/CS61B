package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        //1. check next item for increasing
        //2. when it stops increasing, make that list
        //3. insert it into
        IntListList master= new IntListList();
        IntListList master2=master;
        //master.head= L;
        IntList a= L;
        IntList b= L.tail;
        IntList c= L;
        while(b!=null) {
            if(a.head >= b.head){
                master2.tail= new IntListList(c, null);
                master2=master2.tail;
                c = b;
                a.tail=null;
            }
            a=b;
            b=b.tail;

        }
        master2.tail= new IntListList(c, null);
        return master.tail;
    }
}
