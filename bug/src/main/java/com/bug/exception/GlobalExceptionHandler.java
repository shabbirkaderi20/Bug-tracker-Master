package com.bug.exception;

import com.bug.constant.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException exception) {
        return new ResponseEntity<>(Constant.PROJECT_NOT_EXISTS, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BugNotAddedException.class)
    public ResponseEntity<Object> handleBugNotAddedException(BugNotAddedException exception) {
        return new ResponseEntity<>(Constant.BUG_NOT_ADDED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageNotAddedException.class)
    public ResponseEntity<Object> handleImageNotAddedException(ImageNotAddedException exception) {
        return new ResponseEntity<>(Constant.IMAGE_NOT_ADDED, HttpStatus.BAD_REQUEST);
    }
}
