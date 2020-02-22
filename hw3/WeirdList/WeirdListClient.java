/** Functions to increment and sum the elements of a WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        implementor addfunc= new implementor(n);
        return L.map(addfunc);
    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L) {
        implementor1 sumfunc= new implementor1();
        L.map(sumfunc);
        return sumfunc.retSum();
    }

    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
    public static class implementor implements IntUnaryFunction{

        private int addNum;

        @Override
        public int apply(int x) {
            return addNum+x;
        }
        public implementor(int n){
            addNum = n;

        }
    }
    public static class implementor1 implements IntUnaryFunction{

        private int sum=0;

        @Override
        public int apply(int x) {
            return sum+=x;
        }
        public implementor1(){

        }
        public int retSum(){
            return sum;
        }
    }

}
