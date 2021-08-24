package com.example.watchprog.PayLoad.Response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTTokenSuccessResponse {

    private boolean success;
    private String token;
}
