package ru.sepnotican.printserver;

public class WrongPrinterTypeException extends Exception {
    public WrongPrinterTypeException(String message) {
        super(message);
    }
}
