package com.dating.config;

/**
 * Database configuration properties
 * TODO: Move sensitive credentials to environment variables or external config file
 */
public class DatabaseConfig {
    
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/dating_platform";
    public static final String JDBC_USERNAME = "root";
    public static final String JDBC_PASSWORD = "Admin@123";
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }
}
