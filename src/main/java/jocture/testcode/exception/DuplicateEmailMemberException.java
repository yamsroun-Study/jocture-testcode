package jocture.testcode.exception;

public class DuplicateEmailMemberException extends MemberException {

    public DuplicateEmailMemberException(String message) {
        super(message);
    }

    public DuplicateEmailMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
