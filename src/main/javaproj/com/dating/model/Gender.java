package com.dating.model;

/**
 * Enum representing gender options for users in the dating platform
 */
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Converts a string to a Gender enum value
     * @param value the string representation of gender
     * @return Gender enum value
     * @throws IllegalArgumentException if the value doesn't match any gender
     */
    public static Gender fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Gender value cannot be null");
        }
        
        for (Gender gender : Gender.values()) {
            if (gender.displayName.equalsIgnoreCase(value) || gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender value: " + value);
    }
}
