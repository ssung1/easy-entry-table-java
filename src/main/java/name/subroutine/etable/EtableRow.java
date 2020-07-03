package name.subroutine.etable;

import java.util.*;

/**
 * A record very much like the one in relational databases.
 *
 * Its columns are defined elsewhere and are shared with other records.
 */
public class EtableRow extends AbstractRow {
    public EtableRow(List<Column> columnList) {
        this.columnList = columnList;
        this.valueList = new Vector<>();
    }
}
