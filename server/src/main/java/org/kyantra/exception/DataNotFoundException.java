package org.kyantra.exception;

import javax.ws.rs.NotFoundException;

public class DataNotFoundException extends NotFoundException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
