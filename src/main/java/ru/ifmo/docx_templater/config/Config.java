package ru.ifmo.docx_templater.config;

import ru.ifmo.docx_templater.processor.comment.ICommentProcessor;
import ru.ifmo.docx_templater.wrapper.Paragraph;

import java.util.List;
import java.util.Map;

public class Config {
    private Map<String, List<Class<? extends ICommentProcessor>>> paragraphProcessors;

    public Map<String, List<Class<? extends ICommentProcessor>>> getParagraphProcessors() {
        return paragraphProcessors;
    }

    public void setParagraphProcessors(Map<String, List<Class<? extends ICommentProcessor>>> paragraphProcessors) {
        this.paragraphProcessors = paragraphProcessors;
    }
}
