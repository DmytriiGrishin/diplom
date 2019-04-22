package ru.ifmo.docx_templater.processor;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import ru.ifmo.docx_templater.wrapper.ExpresionRuns;
import ru.ifmo.docx_templater.wrapper.Paragraph;

public class ParagraphTest {


    class NameAndAgeHandler {
        public String name;
        public Integer age;
        NameAndAgeHandler(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

//    @Test
    public void findExpressionInSingleRun() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph xwpfParagraph = document.createParagraph();
        Paragraph paragraph = new Paragraph(xwpfParagraph);
        String expression = "${name}";
        xwpfParagraph.createRun().setText(expression);
        ExpresionRuns expresionRuns = paragraph.findExpression();
        assertEquals(1, expresionRuns.size());
        assertEquals(expression, expresionRuns.get(0).text());
    }
//
//    @Test
//    public void collapseMultipleExpressionInSingleRun() {
//        XWPFDocument document = new XWPFDocument();
//        XWPFParagraph xwpfParagraph = document.createParagraph();
//        Paragraph paragraph = new Paragraph(xwpfParagraph);
//        String expression = "${name} ${age}";
//        xwpfParagraph.createRun().setText(expression);
//        ExpresionRuns expresionRuns = paragraph.findExpression();
//        paragraph.collapseExpressionRuns(expresionRuns);
//
//    }

    @Test
    public void findExpressionInMultipleRuns() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph xwpfParagraph = document.createParagraph();
        Paragraph paragraph = new Paragraph(xwpfParagraph);
        String expression = "${name}";
        xwpfParagraph.createRun().setText("${");
        xwpfParagraph.createRun().setText("name");
        xwpfParagraph.createRun().setText("}");
        ExpresionRuns expresionRuns = paragraph.findExpression();
        assertEquals(3, expresionRuns.size());
        assertEquals(expression, expresionRuns.stream().map(XWPFRun::text).reduce(String::concat).orElse(""));
    }

//    @Test
//    public void collapseRunsTest() {
//        XWPFDocument document = new XWPFDocument();
//        XWPFParagraph xwpfParagraph = document.createParagraph();
//        String expression = "${name}";
//        XWPFRun boldRun = xwpfParagraph.createRun();
//        boldRun.setText("${");
//        boldRun.setBold(true);
//        xwpfParagraph.createRun().setText("name");
//        Paragraph paragraph = new Paragraph(xwpfParagraph);
//        xwpfParagraph.createRun().setText("}");
//        ExpresionRuns expresionRuns = paragraph.findExpression();
//        paragraph.collapseExpressionRuns(expresionRuns);
//        assertEquals(1, xwpfParagraph.getRuns().size());
//        assertEquals(expression, xwpfParagraph.getRuns().get(0).text());
//        assertEquals(true, xwpfParagraph.getRuns().get(0).isBold());
//    }

//    @Test
//    public void replaceInSingleRun() {
//        XWPFDocument document = new XWPFDocument();
//        XWPFParagraph paragraph = document.createParagraph();
//        paragraph.createRun().setText("${name}");
//        String name = "Bob";
//        NameAndAgeHandler nameHandler = new NameAndAgeHandler(name, 1);
//        this.paragraph.process(paragraph, nameHandler);
//        assertEquals(name, paragraph.getText());
//    }
//
//    @Test
//    public void replaceInMultipleRuns() {
//        XWPFDocument document = new XWPFDocument();
//        XWPFParagraph xwpfParagraph = document.createParagraph();
//        Paragraph paragraph = new Paragraph(xwpfParagraph);
//        xwpfParagraph.createRun().setText("${");
//        xwpfParagraph.createRun().setText("name");
//        xwpfParagraph.createRun().setText("}");
//        String name = "Bob";
//        NameAndAgeHandler nameHandler = new NameAndAgeHandler(name, 1);
//        paragraph.process(xwpfParagraph, nameHandler);
//        assertEquals(name, xwpfParagraph.getText());
//    }
//
//    @Test
//    public void replaceMultipleInSingleRun() {
//        XWPFDocument document = new XWPFDocument();
//        XWPFParagraph paragraph = document.createParagraph();
//        paragraph.createRun().setText("${name} ${age}");
//        String name = "Bob";
//        NameAndAgeHandler nameHandler = new NameAndAgeHandler(name, 1);
//        this.paragraph.process(paragraph, nameHandler);
//        assertEquals(name + " " + 1, paragraph.getText());
//    }
}
