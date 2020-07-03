package name.subroutine.etable;

/**
 * Definition of a field.
 */
public class EtableColumn extends ByteColumn {
    public EtableColumn() {
        super();
    }

    public EtableColumn(String name) {
        super(name);
    }

    public EtableColumn(String name, int type) {
        super(name, type);
    }
}
