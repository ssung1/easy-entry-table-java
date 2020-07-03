package name.subroutine.etable;

import java.util.*;

/**
 * A record very much like the one in relational databases.
 *
 * Its fields are defined elsewhere and are shared with other records.
 */
public interface Row {
    /**
     * Returns the contents of the field specified by fld_idx
     */
    public Object get(int fieldIndex);

    /**
     * Returns the contents of the field specified by field name
     */
    public Object get(String name);

    /**
     * Returns internal value list (this is not a copy. alter at your own risk)
     */
    public List<StringBuffer> getValueList();

    /**
     * Sets a value in a record by index
     */
    public Row set(int idx, String value);

    /**
     * Sets a value in a record by index
     */
    public Row set(String field, String value);

    /**
     * Deletes a field and its value
     */
    public Row delete(int idx);

    /**
     * Deletes a field and its value
     */
    public Row delete(String field);

    /**
     * Clears value contents but not field information
     */
    public Row clearValueList();

    /**
     * Returns the number of fields in the record, according to its field data
     */
    public int getColumnCount();

    /**
     * Returns the number of elements in the record, according to its value data
     */
    public int getValueCount();

    /**
     * Returns the number of elements in the record, according to its value data
     */
    public int getSize();

    /**
     * Gets a field by index number
     */
    public Column getColumn(int idx);

    /**
     * Gets a field index by name or -1 if not found
     */
    public int getColumn(String name);

    /**
     * Adds a value at the end of the value list
     *
     * Sometimes our values and field definitions do not match. This is usually a
     * bad thing, but during the construction of a record, we will have these
     * intermediate states.
     */
    public Row push(String val);

    /**
     * Adds the fields of a Record into the current record
     */
    public Row push(Row value);

    /**
     * Adds an array of strings into current record
     */
    public Row pushAll(String[] val);

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
    public Map<String, String> toMap();
}
