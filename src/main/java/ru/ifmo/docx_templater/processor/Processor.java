package ru.ifmo.docx_templater.processor;

@FunctionalInterface
public interface Processor<T> {
    void process(T tag, Object context);
}
