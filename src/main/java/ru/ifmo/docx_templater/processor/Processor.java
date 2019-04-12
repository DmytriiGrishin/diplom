package ru.ifmo.docx_templater.processor;

@FunctionalInterface
public interface Processor<T> {
    /**
     * Evaluates all expressions in tag against context
     * @param tag tag lookup for expressions
     * @param context object to evaluate against
     */
    void process(T tag, Object context);
}
