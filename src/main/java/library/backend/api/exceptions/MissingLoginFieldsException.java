package library.backend.api.exceptions;

public class MissingLoginFieldsException extends RuntimeException {
    private String error;

    public MissingLoginFieldsException(String message) {
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
