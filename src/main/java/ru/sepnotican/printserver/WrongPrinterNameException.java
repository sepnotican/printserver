package ru.sepnotican.printserver;

public class WrongPrinterNameException extends Exception {
    WrongPrinterNameException(String message) {
        super(message);
    }
}
