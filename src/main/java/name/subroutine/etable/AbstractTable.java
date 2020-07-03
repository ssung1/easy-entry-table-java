package name.subroutine.etable;

import java.util.*;
import java.io.*;
import java.sql.*;

public abstract class AbstractTable implements Table {
    /**
     * Current record being accessed
     */
    int _current;

    /**
     * Name of the table
     */
    public String _name;

    /**
     * Type of the table
     */
    public int _type;

    /**
     * This vector contains a list of Field objects. Each field has an offset and a
     * width.
     */
    public Vector _field_lst;

    /**
     * A vector to hold all the record objects
     */
    public Vector _record_lst;

    /**
     * initializes the etable
     */
    public void init() {
        _record_lst = new Vector();
        _field_lst = new Vector();
    }

    /**
     * Clears all contents
     */
    public void clear() {
        _record_lst.clear();
        _field_lst.clear();
    }

    /**
     * Clear only the records
     */
    public void clearRecordLst() {
        _record_lst.clear();
    }

    /**
     * Gets number of fields
     */
    public int fieldCnt() {
        return _field_lst.size();
    }

    /**
     * Gets number of records
     */
    public int recordCnt() {
        return _record_lst.size();
    }

    /**
     * Returns the number of records (alias for recordCnt. not to be overridden)
     */
    public final int size() {
        return recordCnt();
    }

    /**
     * Appends a record at the end of the record set
     */
    public Table push(Record rec) {
        Record nu = createRecord();
        for (int i = 0; i < rec.size(); i++) {
            nu.push(rec.get(i).toString());
        }

        _record_lst.add(nu);

        return this;
    }

    /**
     * Creates a record using given array of String objects.
     *
     * This function is provided for users who are not loading from an etable file
     * but are using the Etable object as a storage area for record sets.
     */
    public Record createRecord(String[] value) {
        Record rec = createRecord();

        int i;
        for (i = 0; i < value.length; i++) {
            rec.push(value[i]);
        }

        return rec;
    }

    /**
     * Creates a record using given List of String objects.
     *
     * This function is provided for users who are not loading from an etable file
     * but are using the Etable object as a storage area for record sets.
     */
    public Record createRecord(List value) {
        String[] string_value = new String[0];

        string_value = (String[]) value.toArray(string_value);

        return createRecord(string_value);
    }

    /**
     * Put the cursor in the first record and returns it
     *
     * @return the first record or null if there are no records
     */
    public Record first() {
        _current = 0;
        if (_record_lst.size() <= 0)
            return null;

        return (Record) _record_lst.elementAt(_current);
    }

    /**
     * Gets the record at current cursor position
     *
     * @return record at current position or null if there are no records
     */
    public Record get() {
        if (_record_lst.size() <= 0)
            return null;

        return (Record) _record_lst.elementAt(_current);
    }

    /**
     * Gets the record with the given record number
     *
     * @param num: record number
     */
    public Record get(int num) {
        if (_record_lst.size() <= num)
            return null;
        return (Record) _record_lst.elementAt(num);
    }

    /**
     * Gets from the current record the field specified by fld_idx
     */
    public Object getVal(int fld_idx) {
        Record rec = get();
        if (rec == null)
            return null;

        return rec.get(fld_idx);
    }

    /**
     * Gets from the current record the field specified by field name
     */
    public Object getVal(String name) {
        Record rec = get();
        if (rec == null)
            return null;

        return rec.get(name);
    }

    /**
     * Gets from the specified record the field specified by fld_idx
     */
    public Object getVal(int rec_idx, int fld_idx) {
        Record rec = get(rec_idx);
        if (rec == null)
            return null;

        return rec.get(fld_idx);
    }

    /**
     * Gets from the current record the field specified by field name
     */
    public Object getVal(int rec_idx, String name) {
        Record rec = get(rec_idx);
        if (rec == null)
            return null;

        return rec.get(name);
    }

    /**
     * sets a value of the current record
     */
    public void setVal(int idx, String val) {
        Record rec = get();
        if (rec == null)
            return;
        rec.set(idx, val);
    }

    /**
     * sets a value of the current record
     */
    public void setVal(String field, String val) {
        Record rec = get();
        if (rec == null)
            return;

        rec.set(field, val);
    }

    /**
     * Advances the cursor to the next record
     */
    public void next() {
        _current++;
    }

    public boolean bof() {
        return _current < 0;
    }

    /**
     * Returns true if the cursor is past last record
     */
    public boolean eof() {
        return (_current >= size());
    }

    /**
     * Sets the current record to the last record and returns it
     *
     * @return last record in the record set
     */
    public Record last() {
        _current = size() - 1;

        if (_current < 0) {
            return null;
        }

        /**
         * Could have used lastElement() too, but this way if the _current value is off,
         * we'd know
         */

        return (Record) _record_lst.elementAt(_current);
    }

    /**
     * Adds a field to the field list
     *
     * The function will create a clone of the given field and adds it to the end of
     * the field list
     */
    public int pushFld(Field field) {
        Field newfield = (Field) field.clone();
        _field_lst.add(newfield);
        return 1;
    }

    /**
     * Adds an array of strings into the field list
     */
    public int pushFld(String[] name_a) {
        for (int i = 0; i < name_a.length; i++) {
            pushFld(name_a[i]);
        }
        return 1;
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
     * This function sets the field list to the provided array of String objects.
     * The field widths and offsets are left at zero
     *
     * @return field list
     */
    public List fieldLst(String[] list) {
        _field_lst = new Vector();

        int i;
        for (i = 0; i < list.length; i++) {
            Field field = createField(list[i].trim());
            _field_lst.add(field);
        }
        return _field_lst;
    }

    /**
     * This function sets the field list to the provided vector of String objects.
     * The field widths and offsets are left at zero
     *
     * @return field list
     */
    public List fieldLst(List list) {
        String[] string_lst = new String[0];
        string_lst = (String[]) list.toArray(string_lst);

        return fieldLst(string_lst);
    }

    /**
     * Returns the field list
     */
    public List fieldLst() {
        return _field_lst;
    }

    /**
     * This function sets the field list to the provided vector of String objects.
     * The field widths and offsets are left at zero
     *
     * @return field list
     */
    public List fieldLst(ResultSetMetaData list) throws SQLException {
        int count;
        count = list.getColumnCount();

        _field_lst = new Vector();

        for (int i = 0; i < count; i++) {
            String column = list.getColumnName(i + 1);

            Field field = createField(column);
            field.init(list, i);

            pushFld(field);
        }
        return _field_lst;
    }

    /**
     * This function sets the field list to the provided vector of String objects.
     * The field widths and offsets are left at zero
     *
     * @return field list
     */
    public List fieldLst(ResultSet list) throws SQLException {
        return fieldLst(list.getMetaData());
    }

    /**
     * This pushes lines in "etable form"
     */
    public void pushLineLst(String[] line_lst) {
        int i;
        for (i = 0; i < line_lst.length; i++) {
            pushLine(line_lst[i]);
        }
    }

    public Table push(String[] value) {
        Record rec;
        rec = createRecord();

        rec.pushLst(value);

        return push(rec);
    }

    /**
     * Adds a record from a result set
     */
    public Table push(java.sql.ResultSet rs) throws SQLException {
        Record rec;
        rec = createRecord();

        rec.pushLst(rs);

        push(rec);

        return this;
    }

    /**
     * Appends an etable with an array Fields must be set first!!! (don't include
     * the fields in the array because it needs field information to append)
     */
    public Table pushLst(String[] value) {
        int cnt;
        cnt = fieldCnt();

        for (int i = 0; i < value.length; i += cnt) {
            Record rec = createRecord();

            for (int j = 0; j < cnt; j++) {
                rec.push(value[i + j]);
            }
            push(rec);
        }

        return this;
    }

    /**
     * Sets the current etable to the contents of data, whose first row contains the
     * field names
     */
    public void set(String[] data, int col_cnt) {
        int row;
        int col;

        int max_row = data.length / col_cnt;

        clear();
        for (row = 0; row < max_row; row++) {
            Record rec = null;

            if (row > 0) {
                rec = createRecord();
                push(rec);
            }
            for (col = 0; col < col_cnt; col++) {
                String val;

                val = data[row * col_cnt + col];
                if (row == 0) {
                    pushFld(val);
                    continue;
                }
                rec.push(val);
            }
        }
    }

    /**
     * Deletes a column
     */
    public Table delete(String name) {
        int idx;
        idx = getFld(name);

        if (idx >= 0) {
            delete(idx);
        }
        return this;
    }

    /**
     * Deletes a column
     */
    public Table delete(int idx) {
        _field_lst.remove(idx);

        for (first(); !eof(); next()) {
            get().valLst().remove(idx);
        }

        return this;
    }

    /**
     * Creates an html table document of this table
     *
     * This function is the equivalent of toHtmlTable( "" )
     */
    public String toHtmlTable() {
        return toHtmlTable("");
    }

    /**
     * creates an html table document of this table
     *
     * @param param is the optional param string after the table tag
     */
    public String toHtmlTable(String param) {
        StringBuffer retval = new StringBuffer();
        List v;
        Iterator e;

        retval.append("<table " + param + ">\n");

        retval.append("<tr>");
        v = _field_lst;
        for (e = v.iterator(); e.hasNext();) {
            retval.append("<th>");
            retval.append(((Field) (e.next())).name());
            retval.append("</th>");
        }
        retval.append("</tr>\n");

        Record rec;
        for (first(); !eof(); next()) {
            rec = get();

            retval.append("<tr>");
            v = rec.valLst();
            for (e = v.iterator(); e.hasNext();) {
                retval.append("<td>");

                String buf;

                buf = e.next().toString();
                if (buf.trim().length() > 0) {
                    retval.append(buf);
                } else {
                    retval.append("&nbsp;");
                }
                retval.append("</td>");
            }
            retval.append("</tr>\n");
        }

        retval.append("</table>\n");

        return retval.toString();
    }

    /**
     * creates an html table document of this table
     *
     * @param tablep is the parameter for the table
     * @param thp    is the parameter for the header
     * @param tdp    is the parameter for the cell
     */
    public String toHtmlTable(String tablep, String thp, String tdp) {
        StringBuffer retval = new StringBuffer();
        List v;
        Iterator e;

        retval.append("<table " + tablep + ">\n");

        retval.append("<tr>");
        v = _field_lst;
        for (e = v.iterator(); e.hasNext();) {
            retval.append("<td " + thp + ">");
            retval.append(((Field) (e.next())).name());
            retval.append("</th>");
        }
        retval.append("</tr>\n");

        Record rec;
        for (first(); !eof(); next()) {
            rec = get();

            retval.append("<tr>");
            v = rec.valLst();
            for (e = v.iterator(); e.hasNext();) {
                retval.append("<td " + tdp + ">");

                String buf;

                buf = e.next().toString();
                if (buf.trim().length() > 0) {
                    retval.append(buf);
                } else {
                    retval.append("&nbsp;");
                }
                retval.append("</td>");
            }
            retval.append("</tr>\n");
        }

        retval.append("</table>\n");

        return retval.toString();
    }

    /**
     * turns a record into an array of 2 x N:
     * 
     * <pre>
     * Field0        Value0
     * Field1        Value1
     * ...
     * and so on
     * </pre>
     */
    public static Object[] toArray(Record rec) {
        int cnt;
        cnt = rec.fieldCnt();

        Object[] retval = new Object[cnt * 2];

        for (int i = 0; i < cnt; i++) {
            retval[i * 2] = rec.getFld(i).name();
            retval[i * 2 + 1] = rec.get(i);
        }

        return retval;
    }

    public int current() {
        return _current;
    }
}
