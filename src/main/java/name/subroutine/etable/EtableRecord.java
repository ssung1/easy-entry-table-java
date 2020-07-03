package name.subroutine.etable;

import java.util.*;
import java.sql.*;

/**
 * A record very much like the one in relational databases.
 *
 * Its fields are defined elsewhere and are shared with other records.
 */
public class EtableRecord extends AbstractRecord {
    public EtableRecord(List field_lst) {
        _field_lst = field_lst;
        _value_lst = new Vector();
    }
}
