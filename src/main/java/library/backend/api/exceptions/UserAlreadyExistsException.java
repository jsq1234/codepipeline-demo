package library.backend.api.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    private String error;

    public UserAlreadyExistsException(String message) {
        super(message);
        error = message;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
