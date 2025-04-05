package kuzmich.exception;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class DaoExceptionTest {

    @Test
    void constructorTest() {
        DaoException daoException = new DaoException(new Throwable("Exception"));
        assertNotNull(daoException);
    }

}