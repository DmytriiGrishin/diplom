package ru.ifmo.docx_templater.wrapper;

import org.apache.poi.xwpf.usermodel.XWPFComment;
import org.apache.xmlbeans.XmlCursor;

public class Comment {
    private XWPFComment comment;
    private XmlCursor startCursor;
    private XmlCursor endCursor;

    public Comment(XWPFComment comment) {
        this.comment = comment;
    }

    public String getId() {
        return comment.getId();
    }

    public String getAuthor() {
        return comment.getAuthor();
    }

    public String getText() {
        return comment.getText();
    }

    public XWPFComment getComment() {
        return comment;
    }

    public void setComment(XWPFComment comment) {
        this.comment = comment;
    }

    public XmlCursor getStartCursor() {
        return startCursor;
    }

    public void setStartCursor(XmlCursor startCursor) {
        this.startCursor = startCursor;
    }

    public XmlCursor getEndCursor() {
        return endCursor;
    }

    public void setEndCursor(XmlCursor endCursor) {
        this.endCursor = endCursor;
    }

    public Comment() {
    }
}
