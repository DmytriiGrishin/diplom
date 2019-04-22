package ru.ifmo.docx_templater;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TemplaterTest {
    @Test
    public void testShowParagraphHide() throws IOException {
        InputStream template = getClass().getResourceAsStream("hideParagraph.docx");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Templater<Object> templater = new Templater<>();
        templater.process(template, new ArrayList<>(), out);
        ByteArrayInputStream result = new ByteArrayInputStream(out.toByteArray());
        XWPFDocument resultDocument = new XWPFDocument(result);
        int size = resultDocument.getParagraphs().size();
        assertEquals(0, size);
        FileOutputStream outputStream = new FileOutputStream("hideParagraphResult.docx");
        resultDocument.write(outputStream);
    }
    @Test
    public void testShowParagraph() throws IOException {
        InputStream template = getClass().getResourceAsStream("showParagraph.docx");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Templater<Object> templater = new Templater<>();
        templater.process(template, new ArrayList<>(), out);
        ByteArrayInputStream result = new ByteArrayInputStream(out.toByteArray());
        XWPFDocument resultDocument = new XWPFDocument(result);
        int size = resultDocument.getParagraphs().size();
        assertEquals(4, size);
        FileOutputStream outputStream = new FileOutputStream("showParagraphResult.docx");
        resultDocument.write(outputStream);
    }
}