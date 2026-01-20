package com.dating.model;

import java.sql.Timestamp;

/**
 * Preferences model class representing user matching preferences
 */
public class Preferences {
    private int userId;
    private String preferredGender; // MALE, FEMALE, OTHER, ANY
    private int minAge;
    private int maxAge;
    private int maxDistance;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public Preferences() {
        this.preferredGender = "ANY";
        this.minAge = 18;
        this.maxAge = 100;
        this.maxDistance = 50;
    }

    public Preferences(int userId, String preferredGender, int minAge, int maxAge, int maxDistance) {
        this.userId = userId;
        this.preferredGender = preferredGender;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.maxDistance = maxDistance;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Preferences{" +
                "userId=" + userId +
                ", preferredGender='" + preferredGender + '\'' +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", maxDistance=" + maxDistance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
