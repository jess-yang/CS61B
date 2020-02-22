/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    private Table input;
    private String colName1;
    private String colName2;

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        this.input = input;
        this.colName1 = colName1;
        this.colName2 = colName2;
    }

    @Override
    protected boolean keep() {
        int numa = input.colNameToIndex(colName1);
        int numb = input.colNameToIndex(colName2);
        String vala = _next.getValue(numa);
        String valb = _next.getValue(numb);
        if (vala == valb) {
            return true;
        } else {
            return false;
        }
    }
    
}
