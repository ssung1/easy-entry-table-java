package name.subroutine.etable;

import java.util.*;

/**
 * A record very much like the one in relational databases.
 *
 * Its fields are defined elsewhere and are shared with other records.
 */
public class EtableRow extends AbstractRow {
    public EtableRow(List<Column> fieldList) {
        this.fieldList = fieldList;
        this.valueList = new Vector<>();
    }
}
