package name.subroutine.etable;

import java.util.*;
import java.sql.*;

/**
 * A record very much like the one in relational databases.
 *
 * Its fields are defined elsewhere and are shared with other records.
 */
public interface Record {
    /**
     * Returns the contents of the field specified by fld_idx
     */
    public Object get(int fld_idx);

    /**
     * Returns the contents of the field specified by field name
     */
    public Object get(String name);

    /**
     * Returns internal value list (this is not a copy. alter at your own risk)
     */
    public List valLst();

    /**
     * Sets a value in a record by index
     */
    public Record set(int idx, String value);

    /**
     * Sets a value in a record by index
     */
    public Record set(String field, String value);

    /**
     * Deletes a field and its value
     */
    public Record delete(int idx);

    /**
     * Deletes a field and its value
     */
    public Record delete(String field);

    /**
     * Clears value contents but not field information
     */
    public Record clearVal();

    /**
     * Returns the number of fields in the record, according to its field data
     */
    public int fieldCnt();

    /**
     * Returns the number of elements in the record, according to its value data
     */
    public int valueCnt();

    /**
     * Returns the number of elements in the record, according to its value data
     */
    public int size();

    /**
     * Gets a field by index number
     */
    public Field getFld(int idx);

    /**
     * Gets a field index by name or -1 if not found
     */
    public int getFld(String name);

    /**
     * Adds a value at the end of the value list
     *
     * Sometimes our values and field definitions do not match. This is usually a
     * bad thing, but during the construction of a record, we will have these
     * intermediate states.
     */
    public Record push(String val);

    /**
     * Adds a value at the end of the value list
     *
     * Sometimes our values and field definitions do not match. This is usually a
     * bad thing, but during the construction of a record, we will have these
     * intermediate states.
     */
    public Record push(Object val);

    /**
     * Adds a set of values to the end of the value list
     */
    public Record pushLst(ResultSet value) throws SQLException;

    /**
     * Adds the fields of a Record into the current record
     */
    public Record push(Record value);

    /**
     * Adds an array of strings into current record
     */
    public Record pushLst(String[] val);

    /**
     * Returns the values as a array of strings
     */
    public String[] toArray();

    /**
     * Returns the record as a string, comma delimited
     */
    public String toString();

    /**
     * Returns the value as a "map", or associative array
     */
    public Map toMap();
}
