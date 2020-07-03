package name.subroutine.etable;

/**
 * This is a field whose smallest unit is the byte
 */
public class ByteColumn extends AbstractColumn {
    public int _offset;
    public int _size;

    public ByteColumn() {
        super();
    }

    public void setSize(int s) {
        _size = s;
    }

    public int getSize() {
        return _size;
    }

    public void setOffset(int o) {
        _offset = o;
    }

    public int getOffset() {
        return _offset;
    }

    public ByteColumn(String name) {
        super(name);
    }

    public ByteColumn(String name, int type) {
        super(name, type);
    }

    public Object clone() {
        ByteColumn nu = new ByteColumn(name.toString());
        return nu;
    }
}
