package cz.knetl.eventmanager.exception;

/**
 *  Base exception for CRUD operation
 * */
public class CRUDException extends Exception {

    public CRUDException() {
    }

    public CRUDException(String message) {
        super(message);
    }
}
