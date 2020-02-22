/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        this.input = input;
        this.index = input.colNameToIndex(colName);
        this.match = match;

    }

    @Override
    protected boolean keep() {
        if (match == _next.getValue(index)) {
            return true;
        }
            return false;
    }

    private Table input;
    private int index;
    private String match;


}
