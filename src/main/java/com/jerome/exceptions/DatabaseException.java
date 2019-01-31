package com.jerome.exceptions;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    public DatabaseException(SQLException e) {
        super(e);
    }
}
