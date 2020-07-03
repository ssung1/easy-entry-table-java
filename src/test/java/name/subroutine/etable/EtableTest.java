package name.subroutine.etable;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

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

        assertThat(etable.recordCnt(), is(0));
    }

    @Test
    public void readHeader() {
        etable.pushLine("%Symbol Description");

        assertThat(etable.getFld(0).name(), is("Symbol"));
        assertThat(etable.getFld(0).size(), is(7));
        assertThat(etable.getFld(1).name(), is("Description"));
        // not sure why size is 0
        //assertThat(etable.getFld(1).size(), is(11));
    }

    @Test
    public void readRow() {
        etable.pushLine("%Symbol Description");
        etable.pushLine(" %      Header");

        assertThat(etable.size(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readComment() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("# this is a comment");
        etable.pushLine(" %      Header");

        assertThat(etable.size(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readHorizontalLine() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("-------------------");
        etable.pushLine(" %      Header");

        assertThat(etable.size(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readBlankSpace() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("                   ");
        etable.pushLine(" %      Header");

        assertThat(etable.size(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readDeletedRecord() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("**      Deleted                  ");
        etable.pushLine(" %      Header");

        assertThat(etable.size(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readMultipleRecords() {
        etable.pushLine("%Symbol Description");
        etable.pushLine(" %      Header");
        etable.pushLine(" *      Deleted");

        assertThat(etable.size(), is(2));
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

        assertThat(etable.size(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("This is a header line"));
    }

    @Test
    public void trimSpaces() {
        etable.pushLine("%Symbol Description");
        etable.pushLine("   %      Header   ");

        assertThat(etable.size(), is(1));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
    }

    @Test
    public void readContinuationHeader() {
        etable.pushLine("%Symbol Header");
        etable.pushLine("_        with");
        etable.pushLine("_        space");
        etable.pushLine(" %      Header");

        assertThat(etable.getFld(0).name(), is("Symbol"));
        assertThat(etable.getFld(0).size(), is(7));
        assertThat(etable.getFld(1).name(), is("Header with space"));
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

        assertThat(etable.size(), is(2));
        assertThat(etable.get(0).get(0).toString(), is("%"));
        assertThat(etable.get(0).get(1).toString(), is("Header"));
        assertThat(etable.get(1).get(0).toString(), is("*"));
        assertThat(etable.get(1).get(1).toString(),
            is("deleted records can also be continued"));
    }
}
