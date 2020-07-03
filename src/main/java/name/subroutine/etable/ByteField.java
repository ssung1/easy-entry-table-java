package name.subroutine.etable;

import java.util.*;

/**
 * This is a field whose smallest unit is the byte
 */
public class ByteField extends AbstractField {
    public int _offset;
    public int _size;

    public ByteField() {
        super();
    }

    public void size(int s) {
        _size = s;
    }

    public int size() {
        return _size;
    }

    public void offset(int o) {
        _offset = o;
    }

    public int offset() {
        return _offset;
    }

    public ByteField(String name) {
        super(name);
    }

    public ByteField(String name, int type) {
        super(name, type);
    }

    public Object clone() {
        ByteField nu = new ByteField(_name.toString());
        return nu;
    }
}
