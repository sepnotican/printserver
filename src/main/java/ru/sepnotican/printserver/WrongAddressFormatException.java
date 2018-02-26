package ru.sepnotican.printserver;

public class WrongAddressFormatException extends Exception {
    WrongAddressFormatException(String message) {
        super(message);
    }
}
