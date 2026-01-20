package com.dating.util;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 100;
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 120;
    
    // Password must contain at least one digit, one lowercase, one uppercase, and one special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    );
    
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");

    private ValidationUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates email format
     * @param email Email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Validates password strength
     * @param password Password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }
        
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Gets password requirements message
     * @return Password requirements as string
     */
    public static String getPasswordRequirements() {
        return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long and contain " +
               "at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=!)";
    }

    /**
     * Validates name (first or last)
     * @param name Name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates age based on date of birth
     * @param dateOfBirth Date of birth
     * @return true if user is 18 or older, false otherwise
     */
    public static boolean isValidAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        int age = Period.between(dateOfBirth, now).getYears();
        
        return age >= MIN_AGE && age <= MAX_AGE;
    }

    /**
     * Validates that two passwords match
     * @param password First password
     * @param confirmPassword Second password
     * @return true if passwords match, false otherwise
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }

    /**
     * Sanitizes input to prevent XSS attacks
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        return input.trim()
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;")
                    .replaceAll("'", "&#x27;")
                    .replaceAll("/", "&#x2F;");
    }
}
