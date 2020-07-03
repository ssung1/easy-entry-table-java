package name.subroutine.etable;

import java.util.*;
import java.io.*;

public interface Table {
    /**
     * initializes the etable
     */
    public void init();

    /**
     * Clears all contents
     */
    public void clear();

    /**
     * Clear only the records
     */
    public void clearRowList();

    /**
     * Gets number of fields
     */
    public int getColumnCount();

    /**
     * Gets number of records
     */
    public int getRowCount();

    /**
     * Gets number of records
     */
    public int getSize();

    /**
     * Appends a record at the end of the record set
     */
    public Table push(Row rec);

    /**
     * Appends an array of String objects at the end of record set. The values need
     * not match the field definitions. This function is provided to those who wish
     * to create a record set
     *
     * This only adds one more record.
     */
    public Table push(String[] value);

    /**
     * Creates a record using given array of String objects.
     *
     * This function is provided for users who are not loading from an etable file
     * but are using the Etable object as a storage area for record sets.
     */
    public Row createRow(String[] value);

    /**
     * Creates a record associated with the table type.
     */
    public Row createRow();

    /**
     * Creates a field associated with the table type.
     */
    public Column createColumn(String name);

    /**
     * Creates a record using given List of String objects.
     *
     * This function is provided for users who are not loading from an etable file
     * but are using the Etable object as a storage area for record sets.
     */
    public Row createRow(List<String> value);

    /**
     * Put the cursor in the first record and returns it
     *
     * @return the first record or null if there are no records
     */
    public Row first();

    /**
     * Gets the record at current cursor position
     *
     * @return record at current position or null if there are no records
     */
    public Row get();

    /**
     * Gets the record with the given record number
     *
     * @param num: record number
     */
    public Row get(int num);

    /**
     * Gets from the current record the field specified by fld_idx
     */
    public Object getValue(int fieldIndex);

    /**
     * Gets from the current record the field specified by field name
     */
    public Object getValue(String name);

    /**
     * Gets from the specified record the field specified by fld_idx
     */
    public Object getValue(int recordIndex, int fieldIndex);

    /**
     * Gets from the current record the field specified by field name
     */
    public Object getValue(int recordIndex, String name);

    /**
     * sets a value of the current record
     */
    public void setValue(int idx, String val);

    /**
     * sets a value of the current record
     */
    public void setValue(String field, String val);

    /**
     * Advances the cursor to the next record
     */
    public void next();

    /**
     * Returns true if the cursor is before first record
     */
    public boolean bof();

    /**
     * Returns true if the cursor is past last record
     */
    public boolean eof();

    /**
     * Sets the current record to the last record and returns it
     *
     * @return last record in the record set
     */
    public Row last();

    /**
     * Adds a field to the field list
     *
     * The function will create a clone of the given field and adds it to the end of
     * the field list
     */
    public int pushColumn(Column field);

    /**
     * Adds a field to the field list by name
     */
    public int pushColumn(String name);

    /**
     * Adds an array of strings into the field list
     */
    public int pushColumn(String[] nameList);

    /**
     * Gets a field by index number
     */
    public Column getColumn(int idx);

    /**
     * Gets a field index by name or -1 if not found
     */
    public int getColumn(String name);

    /**
     * This function sets the field list to the provided array of String objects.
     * The field widths and offsets are left at zero
     *
     * @return field list
     */
    public List<Column> setColumnList(String[] list);

    /**
     * This function sets the field list to the provided vector of String objects.
     * The field widths and offsets are left at zero
     *
     * @return field list
     */
    public List<Column> setColumnList(List<String> list);

    /**
     * Returns the field list
     */
    public List<Column> getColumnList();

    /**
     * Adds a line (must be a complete line, up until linefeed) to the table. The
     * line must include the line identifier as the first character. It will take
     * the appropriate actions.
     */
    public int pushLine(String buf);

    /**
     * Similar to pushLine, but adds an entire file named [fname]
     *
     * This is provided to make life easier.
     *
     * Life is hard. Please die.
     */
    public int pushFile(String fname) throws FileNotFoundException, IOException;

    /**
     * This pushes lines
     */
    public void pushLineList(String[] lineList);

    /**
     * Appends an etable with an array Fields must be set first!!! (don't include
     * the fields in the array because it needs field information to append)
     *
     * This function may add more than one record
     */
    public Table pushList(String[] value);

    /**
     * Sets the current etable to the contents of data, whose first row contains the
     * field names
     */
    public void set(String[] data, int columnCount);

    /**
     * Deletes a column
     */
    public Table delete(String name);

    /**
     * Deletes a column
     */
    public Table delete(int idx);
}
