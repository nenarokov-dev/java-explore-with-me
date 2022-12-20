package ru.practicum.explorewithme.exceptions;

public class UnsupportedBookingStateException extends RuntimeException {
    public UnsupportedBookingStateException(String message) {
        super(message);
    }
}
