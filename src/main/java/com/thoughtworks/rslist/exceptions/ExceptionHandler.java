package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class,IndexOutOfBoundsException.class,InvalidIndexError.class})
    public ResponseEntity<CommentError> handlerExceptions(Exception ex){
        if (ex instanceof MethodArgumentNotValidException){
            CommentError commentError = new CommentError();
            commentError.setError("invalid param");
            return ResponseEntity.badRequest().body(commentError);
        }

        if (ex instanceof InvalidIndexError){
            CommentError commentError = new CommentError();
            commentError.setError("invalid index");
            return ResponseEntity.badRequest().body(commentError);
        }

        if (ex instanceof IndexOutOfBoundsException){
            CommentError commentError = new CommentError();
            commentError.setError("invalid request param");
            return ResponseEntity.badRequest().body(commentError);
        }

        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.badRequest().body(commentError);

    }
}
