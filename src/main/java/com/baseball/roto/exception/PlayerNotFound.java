package com.baseball.roto.exception;

public class PlayerNotFound extends RuntimeException {
    public PlayerNotFound(String message) {
        super(message);
    }
}
