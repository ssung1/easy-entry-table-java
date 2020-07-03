package name.subroutine.etable;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EtableTest {
    private Etable etable = new Etable();

    @Test
    public void canary() {
        return;
    }

    @Test
    public void clearTable() {
        etable.clear();

        assertThat(etable.getRowCount(), is(0));
    }

    @Test
    public void readHeader() {
        etable.pushLine("%Symbol Description");

        assertThat(etable.getColumn(0).getName(), is("Symbol"));
        assertThat(etable.getColumn(0).getSize(), is(7));
        assertThat(etable.getColumn(1).getName(), is("Description"));
        // not sure why size is 0
        //assertThat(etable.getFld(1).size(), is(11));
    }

    @Test
    public void readRow() {
        etable.pushLine("%Symbol Description");
        etable.pushLine(" %      Header");

        assertThat(etable.getSize(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readComment() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("# this is a comment");
        etable.pushLine(" %      Header");

        assertThat(etable.getSize(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readHorizontalLine() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("-------------------");
        etable.pushLine(" %      Header");

        assertThat(etable.getSize(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readBlankSpace() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("                   ");
        etable.pushLine(" %      Header");

        assertThat(etable.getSize(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readDeletedRecord() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("**      Deleted                  ");
        etable.pushLine(" %      Header");

        assertThat(etable.getSize(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readMultipleRecords() {
        etable.pushLine("%Symbol Description");
        etable.pushLine(" %      Header");
        etable.pushLine(" *      Deleted");

        assertThat(etable.getSize(), is(2));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
        assertThat(etable.get(1).get(0).toString(), is("*"));
        assertThat(etable.get(1).get(1).toString(), is("Deleted"));
    }

    @Test
    public void readContinuation() {
        etable.pushLine("%Symbol Description");
        etable.pushLine(" %      This is a ");
        etable.pushLine("_        header line");

        assertThat(etable.getSize(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("This is a header line"));
    }

    @Test
    public void trimSpaces() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("   %      Header   ");

        assertThat(etable.getSize(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readContinuationHeader() {
        etable.pushLine("%Symbol Header");
        etable.pushLine("_        with");
        etable.pushLine("_        space");
        etable.pushLine(" %      Header");

        assertThat(etable.getColumn(0).getName(), is("Symbol"));
        assertThat(etable.getColumn(0).getSize(), is(7));
        assertThat(etable.getColumn(1).getName(), is("Header with space"));
    }

    @Test
    public void readMixedRecords() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("--------------------------");
        etable.pushLine("   %      Header   ");
        etable.pushLine("# ignored   ");
        etable.pushLine("");
        etable.pushLine("        ");
        etable.pushLine("*!      special record");
        etable.pushLine("_        - not supported");
        etable.pushLine(" *      deleted records");
        etable.pushLine("_        can also be continued");

        assertThat(etable.getSize(), is(2));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
        assertThat(etable.get(1).get(0).toString(), is("*"));
        assertThat(etable.get(1).get(1).toString(),
            is("deleted records can also be continued"));
    }

    @Test
    public void clearRowList() {
        etable.pushLine("%Symbol Description");
        etable.pushLine(" %      Header     ");
        etable.pushLine(" *      Deleted    ");

        assertThat(etable.getSize(), is(2));
        etable.clearRowList();
        assertThat(etable.getRowCount(), is(0));
    }

    @Test
    public void pushLineList() {
        etable.pushLineList(new String[] {
            "%Symbol Description",
            " %      Header     ",
            " *      Deleted    ",
        });

        assertThat(etable.getSize(), is(2));
    }

    @Test
    public void getColumnCount() {
        etable.pushLineList(new String[] {
            "%Symbol Description Version",
            " %      Header      1",
            " *      Deleted     1",
        });

        assertThat(etable.getColumnCount(), is(3));
    }

    @Test
    public void pushRow() {
        etable.push(etable.createRow(new String[] {
            "value1",
            "value2",
            "value3",
        }));

        // is still 0 because we created a row without headers
        assertThat(etable.getColumnCount(), is(0));
        assertThat(etable.getRowCount(), is(1));
    }

    @Test
    public void pushStringValues() {
        // create one row using the provided values
        etable.push(new String[] {
            "value1",
            "value2",
            "value3",
        });

        // is still 0 because we created a row without headers
        assertThat(etable.getColumnCount(), is(0));
        assertThat(etable.getRowCount(), is(1));
    }

    @Test
    public void createEmptyrow() {
        Row row = etable.createRow();
        assertThat(row, notNullValue());
        assertThat(row.getSize(), is(0));
    }

    @Test
    public void createColumn() {
        final String name = "some name";
        Column column = etable.createColumn(name);
        assertThat(column, notNullValue());
        assertThat(column.getName(), is(name));
    }

    @Test
    public void traverseThroughRows() {
        etable.pushLineList(new String[] {
            "%Product Price",
            " Hat     1.00",
            " Box     2.00",
        });

        etable.first();
        assertThat(etable.get().get(0).toString(), is("Hat"));
        etable.next();
        assertThat(etable.get().get(0).toString(), is("Box"));
        etable.next();
        assertTrue("Should reach end of file", etable.eof());
    }

    @Test
    public void getValueDirectlyWhileTraversingThroughRows() {
        etable.pushLineList(new String[] {
            "%Product Price",
            " Hat     1.00",
            " Box     2.00",
        });

        etable.first();
        assertThat(etable.getValue(0).toString(), is("Hat"));
        assertThat(etable.getValue("Product").toString(), is("Hat"));
        assertThat(etable.getValue(1).toString(), is("1.00"));
        assertThat(etable.getValue("Price").toString(), is("1.00"));
        etable.next();
        assertThat(etable.getValue(0).toString(), is("Box"));
        assertThat(etable.getValue("Product").toString(), is("Box"));
        assertThat(etable.getValue(1).toString(), is("2.00"));
        assertThat(etable.getValue("Price").toString(), is("2.00"));
        etable.next();
        assertTrue("Should reach end of file", etable.eof());
    }

    @Test
    public void getValueDirectlyUsingRowAndColumn() {
        etable.pushLineList(new String[] {
            "%Product Price",
            " Hat     1.00",
            " Box     2.00",
        });

        assertThat(etable.getValue(0, 0).toString(), is("Hat"));
        assertThat(etable.getValue(0, "Product").toString(), is("Hat"));
        assertThat(etable.getValue(0, 1).toString(), is("1.00"));
        assertThat(etable.getValue(0, "Price").toString(), is("1.00"));

        assertThat(etable.getValue(1, 0).toString(), is("Box"));
        assertThat(etable.getValue(1, "Product").toString(), is("Box"));
        assertThat(etable.getValue(1, 1).toString(), is("2.00"));
        assertThat(etable.getValue(1, "Price").toString(), is("2.00"));
    }

    @Test
    public void setValueWhileTraversingThroughRows() {
        etable.pushLineList(new String[] {
            "%Product Price",
            " Hat     1.00",
            " Box     2.00",
        });

        etable.first();
        etable.setValue(0, "Cat");
        etable.setValue("Price", "3.00");
        etable.next();
        etable.setValue("Product", "Fox");
        etable.setValue(1, "4.00");
        etable.next();
        assertTrue("Should reach end of file", etable.eof());

        etable.first();
        assertThat(etable.getValue(0).toString(), is("Cat"));
        assertThat(etable.getValue("Product").toString(), is("Cat"));
        assertThat(etable.getValue(1).toString(), is("3.00"));
        assertThat(etable.getValue("Price").toString(), is("3.00"));
        etable.next();
        assertThat(etable.getValue(0).toString(), is("Fox"));
        assertThat(etable.getValue("Product").toString(), is("Fox"));
        assertThat(etable.getValue(1).toString(), is("4.00"));
        assertThat(etable.getValue("Price").toString(), is("4.00"));
        etable.next();
        assertTrue("Should reach end of file", etable.eof());
    }

    @Test
    public void pushColumn() {
        final String name = "some column name";
        etable.pushColumn(etable.createColumn(name));
        assertThat(etable.getColumnCount(), is(1));
        assertThat(etable.getColumn(0).getName(), is(name));
    }

    @Test
    public void pushColumnAsString() {
        final String name = "some column name";
        etable.pushColumn(name);
        assertThat(etable.getColumnCount(), is(1));
        assertThat(etable.getColumn(0).getName(), is(name));
    }

    @Test
    public void pushColumnAsStringList() {
        final String nameList[] = {
            "column name 0",
            "column name 1",
        };
        etable.pushColumn(nameList);
        assertThat(etable.getColumnCount(), is(2));
        assertThat(etable.getColumn(0).getName(), is("column name 0"));
        assertThat(etable.getColumn(1).getName(), is("column name 1"));
    }
}
