package name.subroutine.etable;

import java.util.*;

public abstract class AbstractTable implements Table {
    /**
     * Current record being accessed
     */
    int current;

    /**
     * Name of the table
     */
    public String name;

    /**
     * Type of the table
     */
    public int type;

    /**
     * This vector contains a list of Field objects. Each field has an offset and a
     * width.
     */
    public Vector<Column> columnList;

    /**
     * A vector to hold all the record objects
     */
    public Vector<Row> rowList;

    /**
     * initializes the etable
     */
    public void init() {
        rowList = new Vector<>();
        columnList = new Vector<>();
    }

    /**
     * Clears all contents
     */
    public void clear() {
        rowList.clear();
        columnList.clear();
    }

    /**
     * Clear only the records
     */
    public void clearRowList() {
        rowList.clear();
    }

    /**
     * Gets number of fields
     */
    public int getColumnCount() {
        return columnList.size();
    }

    /**
     * Gets number of records
     */
    public int getRowCount() {
        return rowList.size();
    }

    /**
     * Returns the number of records (alias for recordCnt. not to be overridden)
     */
    public final int getSize() {
        return getRowCount();
    }

    /**
     * Appends a record at the end of the record set
     */
    public Table push(Row rec) {
        Row nu = createRecord();
        for (int i = 0; i < rec.getSize(); i++) {
            nu.push(rec.get(i).toString());
        }

        rowList.add(nu);

        return this;
    }

    /**
     * Creates a record using given array of String objects.
     *
     * This function is provided for users who are not loading from an etable file
     * but are using the Etable object as a storage area for record sets.
     */
    public Row createRow(String[] value) {
        Row rec = createRecord();

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
    public Row createRow(List<String> value) {
        String[] string_value = new String[0];

        string_value = (String[]) value.toArray(string_value);

        return createRow(string_value);
    }

    /**
     * Put the cursor in the first record and returns it
     *
     * @return the first record or null if there are no records
     */
    public Row first() {
        current = 0;
        if (rowList.size() <= 0)
            return null;

        return (Row) rowList.elementAt(current);
    }

    /**
     * Gets the record at current cursor position
     *
     * @return record at current position or null if there are no records
     */
    public Row get() {
        if (rowList.size() <= 0)
            return null;

        return (Row) rowList.elementAt(current);
    }

    /**
     * Gets the record with the given record number
     *
     * @param num: record number
     */
    public Row get(int num) {
        if (rowList.size() <= num)
            return null;
        return (Row) rowList.elementAt(num);
    }

    /**
     * Gets from the current record the field specified by fld_idx
     */
    public Object getValue(int columnIndex) {
        Row rec = get();
        if (rec == null)
            return null;

        return rec.get(columnIndex);
    }

    /**
     * Gets from the current record the field specified by field name
     */
    public Object getValue(String name) {
        Row rec = get();
        if (rec == null)
            return null;

        return rec.get(name);
    }

    /**
     * Gets from the specified record the field specified by fld_idx
     */
    public Object getValue(int rowIndex, int columnIndex) {
        Row rec = get(rowIndex);
        if (rec == null)
            return null;

        return rec.get(columnIndex);
    }

    /**
     * Gets from the current record the field specified by field name
     */
    public Object getValue(int rowIndex, String columnName) {
        Row rec = get(rowIndex);
        if (rec == null)
            return null;

        return rec.get(columnName);
    }

    /**
     * sets a value of the current record
     */
    public void setValue(int idx, String val) {
        Row rec = get();
        if (rec == null)
            return;
        rec.set(idx, val);
    }

    /**
     * sets a value of the current record
     */
    public void setValue(String column, String val) {
        Row rec = get();
        if (rec == null)
            return;

        rec.set(column, val);
    }

    /**
     * Advances the cursor to the next record
     */
    public void next() {
        current++;
    }

    public boolean bof() {
        return current < 0;
    }

    /**
     * Returns true if the cursor is past last record
     */
    public boolean eof() {
        return (current >= getSize());
    }

    /**
     * Sets the current record to the last record and returns it
     *
     * @return last record in the record set
     */
    public Row last() {
        current = getSize() - 1;

        if (current < 0) {
            return null;
        }

        /**
         * Could have used lastElement() too, but this way if the _current value is off,
         * we'd know
         */

        return (Row) rowList.elementAt(current);
    }

    /**
     * Adds a field to the field list
     *
     * The function will create a clone of the given field and adds it to the end of
     * the field list
     */
    public int pushColumn(Column field) {
        Column newfield = (Column) field.clone();
        columnList.add(newfield);
        return 1;
    }

    /**
     * Adds an array of strings into the field list
     */
    public int pushColumn(String[] nameList) {
        for (int i = 0; i < nameList.length; i++) {
            pushColumn(nameList[i]);
        }
        return 1;
    }

    /**
     * Gets a field by index number
     */
    public Column getColumn(int idx) {
        Column field = (Column) columnList.get(idx);
        return field;
    }

    /**
     * Gets a field index by name or -1 if not found
     */
    public int getColumn(String name) {
        for (int i = 0; i < columnList.size(); i++) {
            Column field = getColumn(i);
            if (name.equalsIgnoreCase(field.getName())) {
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
    public List<Column> setColumnList(String[] list) {
        columnList = new Vector<>();

        int i;
        for (i = 0; i < list.length; i++) {
            Column field = createField(list[i].trim());
            columnList.add(field);
        }
        return columnList;
    }

    /**
     * This function sets the field list to the provided vector of String objects.
     * The field widths and offsets are left at zero
     *
     * @return field list
     */
    public List<Column> setColumnList(List<String> list) {
        String[] string_lst = new String[0];
        string_lst = (String[]) list.toArray(string_lst);

        return setColumnList(string_lst);
    }

    /**
     * Returns the field list
     */
    public List<Column> getColumnList() {
        return columnList;
    }

    /**
     * This pushes lines in "etable form"
     */
    public void pushLineList(String[] line_lst) {
        int i;
        for (i = 0; i < line_lst.length; i++) {
            pushLine(line_lst[i]);
        }
    }

    public Table push(String[] value) {
        Row rec;
        rec = createRecord();

        rec.pushAll(value);

        return push(rec);
    }

    /**
     * Appends an etable with an array Fields must be set first!!! (don't include
     * the fields in the array because it needs field information to append)
     */
    public Table pushList(String[] value) {
        int cnt;
        cnt = getColumnCount();

        for (int i = 0; i < value.length; i += cnt) {
            Row rec = createRecord();

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
            Row rec = null;

            if (row > 0) {
                rec = createRecord();
                push(rec);
            }
            for (col = 0; col < col_cnt; col++) {
                String val;

                val = data[row * col_cnt + col];
                if (row == 0) {
                    pushColumn(val);
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
        idx = getColumn(name);

        if (idx >= 0) {
            delete(idx);
        }
        return this;
    }

    /**
     * Deletes a column
     */
    public Table delete(int idx) {
        columnList.remove(idx);

        for (first(); !eof(); next()) {
            get().getValueList().remove(idx);
        }

        return this;
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
    public static Object[] toArray(Row rec) {
        int cnt;
        cnt = rec.getColumnCount();

        Object[] retval = new Object[cnt * 2];

        for (int i = 0; i < cnt; i++) {
            retval[i * 2] = rec.getColumn(i).getName();
            retval[i * 2 + 1] = rec.get(i);
        }

        return retval;
    }

    public int current() {
        return current;
    }
}
