package kuzmich.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlConnectionExceptionTest {

    @Test
    void constructorTest() {
        SqlConnectionException sqlConnectionException = new SqlConnectionException(new Throwable("Exception"));
        assertNotNull(sqlConnectionException);
    }

}