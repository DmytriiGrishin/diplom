package ru.ifmo.docx_templater.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RunUtilsTest {

    @Test
    public void copyStyleTest() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun firstRun = paragraph.createRun();
        XWPFRun secondRun = paragraph.createRun();
        firstRun.setBold(true);
        secondRun.setBold(false);
        RunUtils.copyStyle(firstRun, secondRun);
        assertEquals(firstRun.isBold(), secondRun.isBold());
    }

    @Test
    public void splitRunTest() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        String firstRunText = "firstRun";
        String secondRunText = "secondRun";
        XWPFRun firstRun = paragraph.createRun();
        firstRun.setText(firstRunText + secondRunText);
        RunUtils.splitRun(paragraph, firstRun, firstRunText.length());
        assertEquals(2, paragraph.getRuns().size());
        assertEquals(firstRunText + secondRunText, paragraph.getText());
        assertEquals(firstRunText, paragraph.getRuns().get(0).text());
        assertEquals(secondRunText, paragraph.getRuns().get(1).text());

    }

    @Test
    public void concatRunsTest() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        String firstRunText = "firstRun";
        String secondRunText = "secondRun";
        XWPFRun firstRun = paragraph.createRun();
        XWPFRun secondRun = paragraph.createRun();
        firstRun.setText(firstRunText);
        secondRun.setText(secondRunText);
        RunUtils.concatRuns(firstRun, secondRun);
        assertEquals(firstRunText + secondRunText, paragraph.getText());
        assertEquals(firstRunText + secondRunText, paragraph.getRuns().get(0).text());
        assertEquals("", paragraph.getRuns().get(1).text());

    }
}