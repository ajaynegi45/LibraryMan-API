package com.ajaynegi.SpringBoot.RESTBookAPI.ExceptionHandler;

public class BookNotFoundException extends Exception
{
    public BookNotFoundException(String message)
    {
        super(message);
    }
}
