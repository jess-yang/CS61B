/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int count=1;
        if(_front==null&& _back==null){
            return 0;
        }else{
            DNode curr= new DNode(null,_front._val,_front._next);
            while(curr._next!=null){
                count++;
                curr=curr._next;
            }
        }

        return count;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        if(i>=0) {
            DNode front = _front;
            int val = front._val;
            for (int j = 0; j <= i; j++) {
                val = front._val;
                front = front._next;
            }
            return val;
        } else{
            DNode back= _back;
            int val= back._val;
            for(int j=0; j>i; j--){
                val= back._val;
                back= back._prev;
            }
            return val;
        }
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        if(_front==null&& _back==null){
            DNode only= new DNode(d);
            _front=_back=only;
        }
        else {
            DNode front = new DNode(null, d, _front);
            _front._prev = front;
            _front = front;
        }

    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        if(_front==null&& _back==null){
            DNode only= new DNode(d);
            _front=_back=only;
        } else {
            DNode back = new DNode(_back, d, null);
            _back._next = back;
            _back = back;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {

        DNode front= new DNode(null, _front._val, _front._next);
        int frontVal = front._val;
        if(front._prev==null&& front._next==null){
            _front=_back=null;
        } else {
            front._next._prev = null;
            _front = front._next;
        }
        return frontVal;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        DNode back= new DNode(_back._prev,_back._val,null);
        int backVal=back._val;
        if(back._prev==null&& back._next==null){
            _front=_back=null;
        } else {
            back._prev._next = null;
            _back = back._prev;
        }
        return backVal;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        int currIndex=0;
        if(index==0|| -1*index==size()){
            return deleteFront();
        } else if(index==size()-1 || index== -1){
            return deleteBack();
        } else{
            if(index<0) {
                index=size()+index;
            }
                DNode front = new DNode(null, _front._val, _front._next);
                int frontVal = front._val;
                DNode prev = front;
                while (currIndex != index) {
                    prev = front;
                    front = front._next;
                    frontVal = front._val;
                    currIndex++;
                }
                prev._next = front._next;
                front._next._prev = prev;
                return frontVal;

        }
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
