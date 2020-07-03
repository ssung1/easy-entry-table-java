package name.subroutine.etable;

/**
 * Definition of a column.
 */
public interface Column extends Cloneable {
    /**
     * returns the name of this column as a string
     */
    public String getName();

    /**
     * Sets the name of the column 
     */
    public void setName(String n);

    /**
     * returns size
     */
    public void setSize(int s);

    /**
     * Sets type
     */
    public void setType(int t);

    /**
     * Returns type
     */
    public int getType();

    /**
     * returns size
     */
    public int getSize();

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
    public void setOffset(int o);

    /**
     * returns offset
     */
    public int getOffset();

    public Object clone();
}
