package com.example.watchprog.DTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentDTO {
    private long id;
    @NotEmpty
    private String message;
    private String username;

}
