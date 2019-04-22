package ru.ifmo.docx_templater.processor.comment;


import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import ru.ifmo.docx_templater.wrapper.Comment;
import ru.ifmo.docx_templater.wrapper.Document;
import ru.ifmo.docx_templater.wrapper.Paragraph;

@CommentProcessor(targetClass = Paragraph.class, methodPlaceholder = "showParagraphIf")
public class ShowParagraphProcessor implements ICommentProcessor<Paragraph> {
    @Override
    public void process(Paragraph tag, Comment comment, Object context) {
        String text = comment.getText();
        int i = text.indexOf("(");
        String expressionString = text.substring(i+1, text.lastIndexOf(")"));
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(context);
        Expression expression = parser.parseExpression(expressionString);
        Object value = expression.getValue(evaluationContext);
        text = value == null ? "" : value.toString();
        if ( !Boolean.valueOf(text)) {
            Document document = tag.getDocument();
            int posOfParagraph = document.getDocument().getPosOfParagraph(tag.getParagraph());
            document.getDocument().removeBodyElement(posOfParagraph);
        } else {
            tag.removeComment(comment);
        }
    }

}
