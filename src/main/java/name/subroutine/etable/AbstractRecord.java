package name.subroutine.etable;

import java.util.*;
import java.sql.*;

/**
 * A record very much like the one in relational databases.
 *
 * Its fields are defined elsewhere and are shared with other records.
 */
public abstract class AbstractRecord implements Record {
    /**
     * This must be passed from the parent table. It is only included here so that
     * individual records can be handled separately. Also, a fun thing to do is to
     * change the field definitions after the records are loaded. Then all sorts of
     * wacky stuff can happen.
     */
    public List _field_lst;

    /**
     * This is a vector of StringBuffer objects unless stated otherwise.
     */
    public Vector _value_lst;

    public AbstractRecord() {
        _value_lst = new Vector();
    }

    public AbstractRecord(List field_lst) {
        _field_lst = field_lst;
        _value_lst = new Vector();
    }

    /**
     * Returns the contents of the field specified by fld_idx
     */
    public Object get(int fld_idx) {
        return _value_lst.elementAt(fld_idx);
    }

    /**
     * Returns the contents of the field specified by field name
     */
    public Object get(String name) {
        int idx;
        idx = getFld(name);

        if (idx < 0) {
            return new StringBuffer();
        }

        return get(idx);
    }

    public List valLst() {
        return _value_lst;
    }

    /**
     * Sets a value in a record by index
     */
    public Record set(int idx, String value) {
        _value_lst.set(idx, new StringBuffer(value));
        return this;
    }

    /**
     * Sets a value in a record by index
     */
    public Record set(String field, String value) {
        int idx;
        idx = getFld(field);
        return set(idx, value);
    }

    /**
     * Deletes a field and its value
     */
    public Record delete(int idx) {
        _value_lst.remove(idx);
        _field_lst.remove(idx);
        return this;
    }

    /**
     * Deletes a field and its value
     */
    public Record delete(String field) {
        int idx;
        idx = getFld(field);
        return delete(idx);
    }

    public Record clearVal() {
        _value_lst.clear();
        return this;
    }

    /**
     * Returns the number of fields in the record, according to its field data
     */
    public int fieldCnt() {
        return _field_lst.size();
    }

    /**
     * Returns the number of elements in the record, according to its value data
     */
    public int valueCnt() {
        return size();
    }

    /**
     * Returns the number of elements in the record, according to its value data
     */
    public int size() {
        return _value_lst.size();
    }

    /**
     * Gets a field by index number
     */
    public Field getFld(int idx) {
        Field field = (Field) _field_lst.get(idx);
        return field;
    }

    /**
     * Gets a field index by name or -1 if not found
     */
    public int getFld(String name) {
        for (int i = 0; i < _field_lst.size(); i++) {
            Field field = getFld(i);
            if (name.equalsIgnoreCase(field.name())) {
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
    public Record push(String val) {
        _value_lst.addElement(new StringBuffer(val));
        return this;
    }

    /**
     * Adds a value at the end of the value list
     *
     * Sometimes our values and field definitions do not match. This is usually a
     * bad thing, but during the construction of a record, we will have these
     * intermediate states.
     */
    public Record push(Object val) {
        _value_lst.addElement(val);
        return this;
    }

    /**
     * Adds a set of values to the end of the value list
     */
    public Record pushLst(ResultSet value) throws SQLException {
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
    public Record push(Record value) {
        for (int i = 0; i < value.size(); i++) {
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

    public Record pushLst(String[] val) {
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
        String[] array = new String[fieldCnt()];
        int i;
        for (i = 0; i < fieldCnt(); i++) {
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
    public Map toMap() {
        Map map = new HashMap();
        int i;
        try {
            for (i = 0; i < fieldCnt(); i++) {
                String key;
                String val;
                key = getFld(i).name();
                val = get(i).toString();

                map.put(key, val);
            }
        } catch (Exception ex) {
        }

        return map;
    }
}
