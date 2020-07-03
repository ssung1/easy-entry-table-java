package name.subroutine.etable;

import java.sql.*;

/**
 * Definition of a field.
 */
public interface Field extends Cloneable {
    /**
     * Initializes a field by ResultSetMetaData
     *
     * @param i column index
     */
    public void init(ResultSetMetaData list, int i) throws SQLException;

    /**
     * returns the name of this field as a string
     */
    public String name();

    /**
     * Sets the name of the field
     */
    public void name(String n);

    /**
     * returns size
     */
    public void size(int s);

    /**
     * Sets type
     */
    public void type(int t);

    /**
     * Returns type
     */
    public int type();

    /**
     * returns size
     */
    public int size();

    /**
     * Sets precision
     */
    public void prec(int p);

    /**
     * Returns precision
     */
    public int prec();

    /**
     * sets offset
     */
    public void offset(int o);

    /**
     * returns offset
     */
    public int offset();

    public Object clone();
}
