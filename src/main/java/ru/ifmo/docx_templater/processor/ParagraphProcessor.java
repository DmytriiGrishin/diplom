package ru.ifmo.docx_templater.processor;

import java.util.Comparator;
import java.util.Iterator;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ru.ifmo.docx_templater.exceptions.ParsingException;
import ru.ifmo.docx_templater.util.RunUtils;
import ru.ifmo.docx_templater.wrapper.ExpresionHandler;

public class ParagraphProcessor implements Processor<XWPFParagraph> {

    public void process(XWPFParagraph paragraph, Object context) {
        RunProcessor runProcessor = new RunProcessor();
        ExpresionHandler expressions = findExpression(paragraph);
        while (!expressions.isEmpty()) {
            XWPFRun runWithExpression = collapseExpressionRuns(paragraph, expressions);
            runProcessor.process(runWithExpression, context);
            expressions = findExpression(paragraph);
        }
    }

    public XWPFRun collapseExpressionRuns(XWPFParagraph paragraph, ExpresionHandler expressions) {
        XWPFRun expressionStartRun = getRunWithExpressionStart(paragraph, expressions);
        if (expressions.isEmpty()) {
            if (expressionStartRun.text().indexOf("}") == expressionStartRun.text().length() - 1)
                return  expressionStartRun;
            RunUtils.splitRun(paragraph, expressionStartRun, expressionStartRun.text().indexOf("}") + 1);
            return expressionStartRun;
        } else {
            expressions.add(0, expressionStartRun);
            trimLastRun(paragraph, expressions);
            XWPFRun longestRun = getLongestRun(expressions);
            RunUtils.copyStyle(longestRun, expressionStartRun);
            return expressions.stream().reduce(RunUtils::concatRuns).orElseThrow(() -> new ParsingException("No runs with expresion"));
        }
    }

    private void trimLastRun(XWPFParagraph paragraph, ExpresionHandler expressions) {
        XWPFRun lastRun = expressions.remove(expressions.size() - 1);
        if (!lastRun.text().endsWith("}")) {
            RunUtils.splitRun(paragraph, lastRun, lastRun.text().indexOf("}") + 1);
        }
        expressions.add(lastRun);
    }

    /**
     * Returns run with longest text
     * @param expressions runs to lookup
     * @return run with longest text
     */
    private static XWPFRun getLongestRun(ExpresionHandler expressions) {
        return expressions.parallelStream().max(Comparator.comparing(xwpfRun -> xwpfRun.text().length()))
                .orElseThrow(() -> new ParsingException("Cannot find longest run."));
    }

    /**
     * Returns run that starts with "${"
     * @apiNote removes first run from ExpresionHandler
     * @param paragraph paragraph containing runs
     * @param expressions runs containing expresion
     * @return run that starts with "${"
     */
    private static XWPFRun getRunWithExpressionStart(XWPFParagraph paragraph, ExpresionHandler expressions) {
        XWPFRun firstRun = expressions.remove(0);
        int expressionStartIndex = firstRun.text().indexOf("${");
        if (expressionStartIndex < 0) throw new ParsingException("First run of expression handler should contain \"${\".");
        if (expressionStartIndex > 0) {
            return RunUtils.splitRun(paragraph, firstRun, expressionStartIndex);
        } else {
            return firstRun;
        }
    }

    /**
     * Returns ExpresionHandler with runs containing first replacement expresion in paragraph
     * @param paragraph paragraph to lookup
     * @return ExpresionHandler with runs containing first replacement expresion in paragraph. Empty if none found
     */
    public ExpresionHandler findExpression(XWPFParagraph paragraph) {
        ExpresionHandler expresionHandler = new ExpresionHandler();
        Iterator<XWPFRun> runIterator = paragraph.getRuns().iterator();
        while (runIterator.hasNext()) {
            XWPFRun run = runIterator.next();
            if (run.text().contains("${")) {
                expresionHandler.add(run);
                while (!expresionHandler.isCompliteExpression() && runIterator.hasNext()) {
                    expresionHandler.add(runIterator.next());
                }
                break;
            }
        }
        return expresionHandler;
    }
}
