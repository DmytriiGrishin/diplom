package ru.ifmo.docx_templater;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.reflections.Reflections;
import ru.ifmo.docx_templater.config.Config;
import ru.ifmo.docx_templater.processor.comment.CommentProcessor;
import ru.ifmo.docx_templater.processor.comment.ICommentProcessor;
import ru.ifmo.docx_templater.wrapper.Document;
import ru.ifmo.docx_templater.wrapper.Paragraph;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Templater<T> {
    private Config config;

    public Templater() {
        this.config = createDefaultConfig();
    }

    public void process(InputStream template, T context, OutputStream out) throws IOException {
        try (XWPFDocument xwpfDocument = new XWPFDocument(template)) {
            Document document = new Document(xwpfDocument, config);
            document.processParagraphComments(context);
            xwpfDocument.write(out);
        }
    }

    private Config createDefaultConfig() {
        Config config = new Config();
        Reflections reflections = new Reflections("ru.ifmo.docx_templater.processor.comment");
        Set<Class<? extends ICommentProcessor>> commentProcessors = reflections.getSubTypesOf(ICommentProcessor.class);
        Map<String, List<Class<? extends ICommentProcessor>>> paragraphProcessors = commentProcessors.stream()
                .filter(processor -> processor.isAnnotationPresent(CommentProcessor.class))
                .filter(processor -> processor.getAnnotation(CommentProcessor.class).targetClass().equals(Paragraph.class))
                .collect(Collectors.groupingBy(processor -> processor.getAnnotation(CommentProcessor.class).methodPlaceholder()));
        config.setParagraphProcessors(paragraphProcessors);
        return config;
    }

    public Templater(Config config) {
        this.config = config;
    }
}
