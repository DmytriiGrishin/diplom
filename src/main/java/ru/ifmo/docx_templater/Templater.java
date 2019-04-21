package ru.ifmo.docx_templater;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import ru.ifmo.docx_templater.config.Config;
import ru.ifmo.docx_templater.wrapper.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Templater<T> {
    private Config config;

    public Templater() {
        this.config = createDefaultConfig();
    }

    public void proccess(InputStream template, T context, OutputStream out) throws IOException {
        try (XWPFDocument xwpfDocument = new XWPFDocument(template)) {
            Document document = new Document(xwpfDocument);
            document.processParagraphComments(context);
        }
    }

    private Config createDefaultConfig() {
        Config config = new Config();
        return config;
    }

    public Templater(Config config) {
        this.config = config;
    }
}
