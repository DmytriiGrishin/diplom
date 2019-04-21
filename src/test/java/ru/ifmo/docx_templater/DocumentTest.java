package ru.ifmo.docx_templater;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import ru.ifmo.docx_templater.wrapper.Document;

import java.io.IOException;
import java.io.InputStream;

class DocumentTest {

    @Test
    void testCommentsHarvesting() throws IOException {
        InputStream template = this.getClass().getResourceAsStream("multipleComments.docx");
        XWPFDocument xwpfDocument = new XWPFDocument(template);
        Document document = new Document(xwpfDocument);
        document.getComments();
    }
}