package name.subroutine.etable;

import java.util.*;
import java.sql.*;
import java.lang.reflect.*;

/**
 * Definition of a field.
 */
public abstract class AbstractField implements Field {
    public StringBuffer _name;
    public int _type;

    public AbstractField() {
        _name = new StringBuffer();
        _type = -1;
    }

    public AbstractField(String name, int type) {
        _name = new StringBuffer(name);
        _type = type;
    }

    public AbstractField(String name) {
        _name = new StringBuffer(name);
        _type = -1;
    }

    public void init(ResultSetMetaData list, int i) throws SQLException {
        String column;
        column = list.getColumnName(i + 1);
        int type;
        type = list.getColumnType(i + 1);
        int size;
        size = list.getColumnDisplaySize(i + 1);

        // 10/15/2000
        //
        // we use only the name after the period, because
        // some databases keep the table name in the column
        // name too

        int period;
        period = column.lastIndexOf('.');
        if (period >= 0) {
            column = column.substring(period + 1);
        }

        name(column);

        if (type == java.sql.Types.CHAR) {
            type('C');
            size(size);
        } else if (type == java.sql.Types.VARCHAR) {
            type('C');
            size(size);
        } else if (type == java.sql.Types.INTEGER) {
            type('N');
            size(11);
        } else if (type == java.sql.Types.SMALLINT) {
            type('N');
            size(6);
        }
    }

    /**
     * returns the name of this field as a string
     */
    public String name() {
        return _name.toString();
    }

    public void name(String n) {
        _name = new StringBuffer(n);
    }

    /**
     * Sets type
     */
    public void type(int t) {
        _type = t;
    }

    /**
     * Sets type by Class
     */
    public void type(Class t) {
        if (t.equals(int.class) || t.equals(double.class) || t.equals(float.class)) {
            _type = 'N';
            return;
        }

        if (t.equals(java.util.Date.class)) {
            _type = 'D';
            return;
        }

        _type = 'C';
    }

    /**
     * Returns type
     */
    public int type() {
        return _type;
    }

    /**
     * Does nothing. Subclasses should decide if prec is supported for that class of
     * field
     */
    public void prec(int p) {
    }

    /**
     * Does nothing. Subclasses should decide if prec is supported for that class of
     * field
     */
    public int prec() {
        return 0;
    }

    public Object clone() {
        try {
            Class c = getClass();

            Constructor con;
            con = c.getConstructor(new Class[] { String.class, int.class });

            Object nu = con.newInstance(new Object[] { _name.toString(), new Integer(_type) });
            return nu;
        } catch (Exception ex) {
            return null;
        }
    }
}
