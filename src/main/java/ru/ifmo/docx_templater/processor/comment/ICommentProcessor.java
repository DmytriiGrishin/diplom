package ru.ifmo.docx_templater.processor.comment;

import ru.ifmo.docx_templater.wrapper.Comment;

public interface ICommentProcessor<T> {
    void process(T tag, Comment comment, Object context);
}
