/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        this.input = input;
        this.index = input.colNameToIndex(colName);
        this.ref = ref;
    }

    @Override
    protected boolean keep() {
        if (_next.getValue(index).compareTo(ref) > 0) {
            return true;
        } else{
            return false;
        }
    }

    private Table input;
    private int index;
    private String ref;
}
