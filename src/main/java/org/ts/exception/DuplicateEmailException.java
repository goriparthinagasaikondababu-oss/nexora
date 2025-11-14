package org.ts.exception;

public class DuplicateEmailException extends Exception{
    public DuplicateEmailException(String message){
        super(message);
    }
}
