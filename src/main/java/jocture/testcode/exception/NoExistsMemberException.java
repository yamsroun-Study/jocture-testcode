package jocture.testcode.exception;

public class NoExistsMemberException extends MemberException {
    
    public NoExistsMemberException(String message) {
        super(message);
    }

    public NoExistsMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
