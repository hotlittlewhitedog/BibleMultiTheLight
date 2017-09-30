
package org.hlwd.bible;

//bNumber INTEGER, cNumber INTEGER, vNumber INTEGER, changeDt TEXT, mark INTEGER, note TEXT,
class NoteBO
{
    final int bNumber;
    final int cNumber;
    final int vNumber;
    final String changeDt;
    final int mark;
    final String note = "";     //DEFAULT

    NoteBO(final int bNumber, final int cNumber, final int vNumber, final String changeDt, final int mark)
    {
        this.bNumber = bNumber;
        this.cNumber = cNumber;
        this.vNumber = vNumber;
        this.changeDt = changeDt;
        this.mark = mark;
    }
}
