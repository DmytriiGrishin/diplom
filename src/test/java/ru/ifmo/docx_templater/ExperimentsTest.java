package ru.ifmo.docx_templater;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

public class ExperimentsTest {

    @Test
    public void OpenFileTest() throws IOException {
        InputStream template = getClass().getResourceAsStream("commentTest.docx");
        XWPFDocument xwpfDocument = new XWPFDocument(template);
        xwpfDocument.getComments();
        xwpfDocument.getParagraphs().get(0).getCTP().getDomNode().getChildNodes().item(1).getAttributes().getNamedItem("w:id").getNodeValue();
    }
}
