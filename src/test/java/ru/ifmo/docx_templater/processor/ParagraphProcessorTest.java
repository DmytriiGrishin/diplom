package ru.ifmo.docx_templater.processor;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import ru.ifmo.docx_templater.wrapper.ExpresionHandler;

public class ParagraphProcessorTest {

    private final ParagraphProcessor paragraphProcessor = new ParagraphProcessor();

    class NameAndAgeHandler {
        public String name;
        public Integer age;
        NameAndAgeHandler(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    public void findExpressionInSingleRun() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        String expression = "${name}";
        paragraph.createRun().setText(expression);
        ExpresionHandler expresionHandler = paragraphProcessor.findExpression(paragraph);
        assertEquals(1, expresionHandler.size());
        assertEquals(expression, expresionHandler.get(0).text());
    }

    @Test
    public void collapseMultipleExpressionInSingleRun() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        String expression = "${name} ${age}";
        paragraph.createRun().setText(expression);
        ExpresionHandler expresionHandler = paragraphProcessor.findExpression(paragraph);
        paragraphProcessor.collapseExpressionRuns(paragraph,expresionHandler);

    }

    @Test
    public void findExpressionInMultipleRuns() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        String expression = "${name}";
        paragraph.createRun().setText("${");
        paragraph.createRun().setText("name");
        paragraph.createRun().setText("}");
        ExpresionHandler expresionHandler = paragraphProcessor.findExpression(paragraph);
        assertEquals(3, expresionHandler.size());
        assertEquals(expression, expresionHandler.stream().map(XWPFRun::text).reduce(String::concat).orElse(""));
    }

    @Test
    public void collapseRunsTest() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        String expression = "${name}";
        paragraph.createRun().setText("${");
        XWPFRun boldRun = paragraph.createRun();
        boldRun.setText("name");
        boldRun.setBold(true);
        paragraph.createRun().setText("}");
        ExpresionHandler expresionHandler = paragraphProcessor.findExpression(paragraph);
        paragraphProcessor.collapseExpressionRuns(paragraph, expresionHandler);
        assertEquals(3, paragraph.getRuns().size());
        assertEquals(expression, paragraph.getRuns().get(0).text());
        assertEquals(true, paragraph.getRuns().get(0).isBold());
    }

    @Test
    public void replaceInSingleRun() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.createRun().setText("${name}");
        String name = "Bob";
        NameAndAgeHandler nameHandler = new NameAndAgeHandler(name, 1);
        paragraphProcessor.process(paragraph, nameHandler);
        assertEquals(name, paragraph.getText());
    }

    @Test
    public void replaceInMultipleRuns() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.createRun().setText("${");
        paragraph.createRun().setText("name");
        paragraph.createRun().setText("}");
        String name = "Bob";
        NameAndAgeHandler nameHandler = new NameAndAgeHandler(name, 1);
        paragraphProcessor.process(paragraph, nameHandler);
        assertEquals(name, paragraph.getText());
    }

    @Test
    public void replaceMultipleInSingleRun() {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.createRun().setText("${name} ${age}");
        String name = "Bob";
        NameAndAgeHandler nameHandler = new NameAndAgeHandler(name, 1);
        paragraphProcessor.process(paragraph, nameHandler);
        assertEquals(name + " " + 1, paragraph.getText());
    }
}
