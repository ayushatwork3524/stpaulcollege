package com.main.stpaul.Exceptions;

public class UnauthorizeException extends RuntimeException{
    public UnauthorizeException(String message){
        super(message);
    }
}