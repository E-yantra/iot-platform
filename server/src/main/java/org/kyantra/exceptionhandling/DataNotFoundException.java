package org.kyantra.exceptionhandling;

import javax.ws.rs.NotFoundException;

public class DataNotFoundException extends NotFoundException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
