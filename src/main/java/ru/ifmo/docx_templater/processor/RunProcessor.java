package ru.ifmo.docx_templater.processor;

import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class RunProcessor implements Processor<XWPFRun> {
    @Override
    public void process(XWPFRun tag, Object context) {
        String text = tag.text();
        text = text.replaceAll("^\\$\\{", "").replaceAll("\\}$", "");
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(context);
        Expression expression = parser.parseExpression(text);
        Object value = expression.getValue(evaluationContext);
        text = value == null ? "" : value.toString();
        tag.setText(text, 0);
    }
}
