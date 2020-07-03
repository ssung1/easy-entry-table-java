package name.subroutine.etable;

import java.lang.reflect.*;

/**
 * Definition of a column.
 */
public abstract class AbstractColumn implements Column {
    public StringBuffer name;
    public int type;

    public AbstractColumn() {
        name = new StringBuffer();
        type = -1;
    }

    public AbstractColumn(String name, int type) {
        this.name = new StringBuffer(name);
        this.type = type;
    }

    public AbstractColumn(String name) {
        this.name = new StringBuffer(name);
        this.type = -1;
    }

    /**
     * returns the name of this column as a string
     */
    public String getName() {
        return name.toString();
    }

    public void setName(String n) {
        name = new StringBuffer(n);
    }

    /**
     * Sets type
     */
    public void setType(int t) {
        type = t;
    }

    /**
     * Returns type
     */
    public int getType() {
        return type;
    }

    /**
     * Does nothing. Subclasses should decide if prec is supported for that class of
     * column 
     */
    public void prec(int p) {
    }

    /**
     * Does nothing. Subclasses should decide if prec is supported for that class of
     * column 
     */
    public int prec() {
        return 0;
    }

    public Object clone() {
        try {
            Class<? extends AbstractColumn> c = getClass();

            Constructor<? extends AbstractColumn> con;
            con = c.getConstructor(new Class[] { String.class, int.class });

            Object nu = con.newInstance(new Object[] { name.toString(), new Integer(type) });
            return nu;
        } catch (Exception ex) {
            return null;
        }
    }
}
