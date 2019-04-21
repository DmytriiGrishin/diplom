package ru.ifmo.docx_templater.wrapper;

import java.util.Iterator;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ru.ifmo.docx_templater.exceptions.ParsingException;
import ru.ifmo.docx_templater.util.RunUtils;

public class Paragraph  {

    private XWPFParagraph paragraph;
    private Document document;

    public Paragraph(XWPFParagraph paragraph) {
        this.paragraph =  paragraph;
    }

    public void fixRuns() {

        ExpresionRuns expression = findExpression();
        while (!expression.isEmpty()) {
            collapseExpressionRuns(expression);
            expression = findExpression();
        }
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Splits run text at index, adding new run right after current with same style.
     * @param run run to split
     * @param at index in run text to split
     * @return new run
     */

    private XWPFRun splitRun(XWPFRun run, int at) {
        XWPFRun newRun;
        int i = paragraph.getRuns().indexOf(run);
        newRun = paragraph.insertNewRun(i+1);
        String remainingText = run.text().substring(at);
        newRun.setText(remainingText);
        run.setText(run.text().substring(0, at), 0);
        RunUtils.copyStyle(run, newRun);
        return newRun;
    }

    /**
     * Concats runs by adding all text of right run to left run and setting right run text to empty string
     * @param left run to contain all text
     * @param right run to be empty
     * @return left run
     */
    public XWPFRun concatRuns(XWPFRun left, XWPFRun right) {
        left.setText(left.text() + right.text(), 0);
        int rightParagraphIndex = paragraph.getRuns().indexOf(right);
        paragraph.removeRun(rightParagraphIndex);
        return left;
    }

    /**
     * Collapses runs to single run, starting with expresion start placeholder and ending with expression end placeholder
     * @param expressions list of runs to collapse
     * @return run with expression
     */

    XWPFRun collapseExpressionRuns(ExpresionRuns expressions) {
        XWPFRun expressionStartRun = getRunWithExpressionStart(expressions);
        if (expressions.size() == 1) {
            if (expressionStartRun.text().indexOf("}") == expressionStartRun.text().length() - 1)
                return  expressionStartRun;
            splitRun(expressionStartRun, expressionStartRun.text().indexOf("}") + 1);
            return expressionStartRun;
        } else {
            trimLastRun(expressions);
            return expressions.stream().reduce(this::concatRuns).orElseThrow(() -> new ParsingException("No runs with expresion"));
        }
    }

    /**
     * Splits last run in expressions so it would end with "}"
     * @param expressions list of runs
     */
    private void trimLastRun(ExpresionRuns expressions) {
        XWPFRun lastRun = expressions.get(expressions.size() - 1);
        if (!lastRun.text().endsWith("}")) {
            splitRun(lastRun, lastRun.text().indexOf("}") + 1);
        }
    }

    /**
     * Returns run that starts with "${"
     * @param expressions runs containing expresion
     * @return run that starts with "${"
     */
    private XWPFRun getRunWithExpressionStart(ExpresionRuns expressions) {
        XWPFRun firstRun = expressions.get(0);
        int expressionStartIndex = firstRun.text().indexOf("${");
        if (expressionStartIndex < 0) throw new ParsingException("First run of expression handler should contain \"${\".");
        if (expressionStartIndex > 0) {
            XWPFRun splitedRun = splitRun(firstRun, expressionStartIndex);
            expressions.remove(firstRun);
            expressions.add(0, splitedRun);
            return splitedRun;
        } else {
            return firstRun;
        }
    }

    /**
     * Returns ExpresionRuns with runs containing first replacement expresion in paragraph
     * @return ExpresionRuns with runs containing first replacement expresion in paragraph. Empty if none found
     */
    public ExpresionRuns findExpression() {
        ExpresionRuns expresionRuns = new ExpresionRuns();
        Iterator<XWPFRun> runIterator = paragraph.getRuns().iterator();
        while (runIterator.hasNext()) {
            XWPFRun run = runIterator.next();
            if (run.text().startsWith("${")
                    && run.text().endsWith("}")
                    && run.text().indexOf("}") == run.text().lastIndexOf("}")) {
                continue;
            }
            if (run.text().contains("${")) {
                expresionRuns.add(run);
                while (!expresionRuns.isCompleteExpression() && runIterator.hasNext()) {
                    expresionRuns.add(runIterator.next());
                }
                break;
            }
        }
        return expresionRuns;
    }

}
