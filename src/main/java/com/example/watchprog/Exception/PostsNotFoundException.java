package com.example.watchprog.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PostsNotFoundException extends RuntimeException {
    public PostsNotFoundException(String msgs) {
        super(msgs);
    }
}
