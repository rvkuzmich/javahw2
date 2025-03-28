package kuzmich.exception;

public class SqlConnectionException extends RuntimeException {
    public SqlConnectionException(Throwable cause) {
        super(cause);
    }
}
