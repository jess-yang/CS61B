/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        this.input = input;
        this.index = input.colNameToIndex(colName);
        this.subStr = subStr;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(index).contains(subStr);
    }

    private Table input;
    private String subStr;
    private int index;
}
