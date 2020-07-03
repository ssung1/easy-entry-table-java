# Easy Entry Table

Easy Entry Table is an easy way to create table from text, much like the
Markdown table, with some differences:

1. Instead of using delimiter, Easy Entry Table uses fixed length
columns, defined by the header row.
2. It is possible to continue a table cell on a different line, making
the source text easier to read and allowing the source text to be
within a character limit per line.

## General Description

The easy entry table is a text file with records separated by
newlines.  The first character in each line is the identifier,
dictating how the line should be interpreted.  The fields in some
respect are considered to be fixed length.  The length of each field
is defined by the header record.

The fields in the header record are defined by words (continuous
sequence of non-space characters).  The beginning of a word is the
beginning of each field.  There are no gaps between fields.  The field
width is the distance between one field and the next.  Note that this
would imply that each field name be separated by at least one space,
and thus the minimum length of a field is two.

The continuation record is used to extend the contents of a previous
record, which can either be a data record or a header record.  New
fields are not defined when processing continuation records.  Instead,
the fields previously defined by a header record is used to separate
the record.  The contents of the fields are then concatenated onto the
end of the previous record.  This would allow each field to have
unlimited width.

Comments and horizontal rules cannot be "continued".  If a
continuation record follows a comment, it will be interpreted as a
continuation of the closest previous record that is not a comment or a
horizontal rule.

### Example

An example of an easy entry table looks like this:

```text
%Unit     Size   Food Mineral Gas Armor HP   GAttack  AAttack Range
-----------------------------------------------------------------------
 battle   L      8    400     300 3     500  25       25      6
_cruiser
 marine   S      1    50      0   0     40   6        6       4/5 upg
```

### Record Identifiers

|Symbol|Description|
|------|-----------|
|%|A header.  This describes the field names.  The beginning of each field is the transition point between a space and a non-space.  Thus, field names cannot contain spaces.|
|#|A comment.  This line is ignored.|
|-|A horizontal line, also ignored.|
|space|An actual space, not the word "space".  This is a regular record.  Each line will be separated into fields defined by the most recent header line.  Beginning and trailing spaces are trimmed.|
|\*|A deleted record.  It is the same as a record but marked for deletion.|
|\_|This means a continuation of a previous line.  The information will be parsed in the same way as the previous line, with the exception that trimming will only be done to the trailing spaces.  The contents will the nbe appended to the previous record, without inserting additional spaces in between.|

Just imagine writing above table using Easy Entry Table format.  The source text would be so much easier to read.

### Notes

If a line contains nothing but whitespace, then the line is ignored.

Lines do not need to have uniform length.  In the header, the end of
the last field is defined by a newline, making the last field a more
flexible field.  Use it wisely.

Header can be separated into multiple lines.  The same rules of
trimming and concatenation apply.

