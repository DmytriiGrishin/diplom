package ru.ifmo.docx_templater.wrapper;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class Document {
    private XWPFDocument document;
    private List<Paragraph> paragraphs;
    private Map<String, Comment> comments;

    public Document(XWPFDocument document) {
        this.document = document;
        paragraphs = document.getParagraphs().stream()
                .map(Paragraph::new)
                .peek(Paragraph::fixRuns)
                .peek(p -> p.setDocument(this))
                .collect(toList());
        comments = collectComments();
    }

    private Map<String, Comment> collectComments() {
        Map<String, Comment> comments = Arrays.stream(document.getComments())
                .map(Comment::new)
                .collect(toMap(Comment::getId, identity()));
        document.getParagraphs().stream()
                .map(XWPFParagraph::getCTP)
                .map(CTP::getCommentRangeStartList)
                .flatMap(List::stream)
                .forEach(ctMarkupRange -> {
                    String id = ctMarkupRange.getDomNode().getAttributes().getNamedItem("w:id").getNodeValue();
                    comments.get(id).setStartCursor(ctMarkupRange.newCursor());
                });
        document.getParagraphs().stream()
                .map(XWPFParagraph::getCTP)
                .map(CTP::getCommentRangeEndList)
                .flatMap(List::stream)
                .forEach(ctMarkupRange -> {
                    String id = ctMarkupRange.getDomNode().getAttributes().getNamedItem("w:id").getNodeValue();
                    comments.get(id).setEndCursor(ctMarkupRange.newCursor());
                });
        return comments;
    }

    public XWPFDocument getDocument() {
        return document;
    }

    public void setDocument(XWPFDocument document) {
        this.document = document;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public Map<String, Comment> getComments() {
        return comments;
    }

    public void setComments(Map<String, Comment> comments) {
        this.comments = comments;
    }

    public <T> void processParagraphComments(T contextRoot) {

    }
}
