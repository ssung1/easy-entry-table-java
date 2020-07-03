package name.subroutine.etable;

import java.util.*;
import java.sql.*;

/**
 * A record very much like the one in relational databases.
 *
 * Its fields are defined elsewhere and are shared with other records.
 */
public abstract class AbstractRow implements Row {
    /**
     * This must be passed from the parent table. It is only included here so that
     * individual records can be handled separately. Also, a fun thing to do is to
     * change the field definitions after the records are loaded. Then all sorts of
     * wacky stuff can happen.
     */
    public List<Column> fieldList;

    /**
     * This is a vector of StringBuffer objects unless stated otherwise.
     */
    public Vector<StringBuffer> valueList;

    public AbstractRow() {
        valueList = new Vector<>();
    }

    public AbstractRow(List<Column> fieldList) {
        this.fieldList = fieldList;
        valueList = new Vector<>();
    }

    /**
     * Returns the contents of the field specified by fld_idx
     */
    public Object get(int fieldIndex) {
        return valueList.elementAt(fieldIndex);
    }

    /**
     * Returns the contents of the field specified by field name
     */
    public Object get(String name) {
        int idx;
        idx = getColumn(name);

        if (idx < 0) {
            return new StringBuffer();
        }

        return get(idx);
    }

    public List<StringBuffer> getValueList() {
        return valueList;
    }

    /**
     * Sets a value in a record by index
     */
    public Row set(int idx, String value) {
        valueList.set(idx, new StringBuffer(value));
        return this;
    }

    /**
     * Sets a value in a record by index
     */
    public Row set(String field, String value) {
        int idx;
        idx = getColumn(field);
        return set(idx, value);
    }

    /**
     * Deletes a field and its value
     */
    public Row delete(int idx) {
        valueList.remove(idx);
        fieldList.remove(idx);
        return this;
    }

    /**
     * Deletes a field and its value
     */
    public Row delete(String field) {
        int idx;
        idx = getColumn(field);
        return delete(idx);
    }

    public Row clearValueList() {
        valueList.clear();
        return this;
    }

    /**
     * Returns the number of fields in the record, according to its field data
     */
    public int getColumnCount() {
        return fieldList.size();
    }

    /**
     * Returns the number of elements in the record, according to its value data
     */
    public int getValueCount() {
        return getSize();
    }

    /**
     * Returns the number of elements in the record, according to its value data
     */
    public int getSize() {
        return valueList.size();
    }

    /**
     * Gets a field by index number
     */
    public Column getColumn(int idx) {
        Column field = (Column) fieldList.get(idx);
        return field;
    }

    /**
     * Gets a field index by name or -1 if not found
     */
    public int getColumn(String name) {
        for (int i = 0; i < fieldList.size(); i++) {
            Column field = getColumn(i);
            if (name.equalsIgnoreCase(field.getName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds a value at the end of the value list
     *
     * Sometimes our values and field definitions do not match. This is usually a
     * bad thing, but during the construction of a record, we will have these
     * intermediate states.
     */
    public Row push(String val) {
        valueList.addElement(new StringBuffer(val));
        return this;
    }

    /**
     * Adds a set of values to the end of the value list
     */
    public Row pushLst(ResultSet value) throws SQLException {
        int count;
        ResultSetMetaData md;
        md = value.getMetaData();
        count = md.getColumnCount();
        for (int i = 0; i < count; i++) {
            String v = value.getString(i + 1);
            if (v == null) {
                push("");
            } else {
                push(v.trim());
            }
        }
        return this;
    }

    /**
     * Adds the fields of a Record into the current record
     */
    public Row push(Row value) {
        for (int i = 0; i < value.getSize(); i++) {
            Object obj;
            obj = value.get(i);

            if (obj == null) {
                push("");
            } else {
                push(obj.toString());
            }
        }
        return this;
    }

    public Row pushAll(String[] val) {
        int i;
        for (i = 0; i < val.length; i++) {
            push(val[i]);
        }
        return this;
    }

    /**
     * Returns the values as a array of strings
     */
    public String[] toArray() {
        String[] array = new String[getColumnCount()];
        int i;
        for (i = 0; i < getColumnCount(); i++) {
            Object v = get(i);
            if (v == null) {
                array[i] = "";
            } else {
                array[i] = v.toString();
            }
        }
        return array;
    }

    /**
     * Returns the value as a "map", or associative array
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        int i;
        try {
            for (i = 0; i < getColumnCount(); i++) {
                String key;
                String val;
                key = getColumn(i).getName();
                val = get(i).toString();

                map.put(key, val);
            }
        } catch (Exception ex) {
        }

        return map;
    }
}
