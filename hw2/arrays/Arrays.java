package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Jessica Yang
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        if(A==null){
            return B;
        } else if(B==null){
            return A;
        }
        int aLen= A.length;
        int bLen= B.length;
        int sum= aLen+bLen;
        int[] sol= new int[sum];
        for(int k=0; k<aLen; k++){
            sol[k]= A[k];
        }
        for(int j=aLen; j<sum; j++){
            sol[j]= B[j-aLen];
        }
        return sol;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int newLen= A.length-len;
        int[] finalArray= new int[newLen];
        for(int k=0; k<start; k++){
            finalArray[k]=A[k]; //{0,1,2,3,4,5,6}
        }
        for(int k=start+len; k<A.length; k++){
            finalArray[k-len]=A[k];
        }

        return finalArray;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        // catenate arrays onto the end
        // remove ?
        int[][] ret= new int[1][];
        while(A!=null){
            int lastIndex=0;
            for(int i=0; i<A.length-1; i++){ //finding first cutoff index{1, 3, **7, 5, 4, 6, 9, 10}
                if (A[i] >= A[i+1]){
                    lastIndex=i;
                }
            }
            int[] section= new int[lastIndex+1];
            for(int i=0; i<lastIndex+1; i++){
                section[i]=A[i];
            }
            A= remove(A, 0,lastIndex+1);


            ret=catenate2D(section, null);
        }
        return ret;
    }
    static int[][] catenate2D(int[] A, int[] B) {
        int[][] newB= new int[][]{B};
        int[][] newA= new int[][]{A};
        if(A==null){
            return newB;
        } else if(B==null){
            return newA;
        }
        int[][] ret= new int[][]{A,B};

        return ret;
    }
}
