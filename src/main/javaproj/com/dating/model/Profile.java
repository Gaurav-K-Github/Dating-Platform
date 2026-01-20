package com.dating.model;

import java.sql.Timestamp;

/**
 * Profile model class representing user profile information
 */
public class Profile {
    private int profileId;
    private int userId;
    private String bio;
    private String location;
    private String profilePhoto;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public Profile() {
    }

    public Profile(int userId, String bio, String location, String profilePhoto) {
        this.userId = userId;
        this.bio = bio;
        this.location = location;
        this.profilePhoto = profilePhoto;
    }

    public Profile(int profileId, int userId, String bio, String location, String profilePhoto, 
                   Timestamp createdAt, Timestamp updatedAt) {
        this.profileId = profileId;
        this.userId = userId;
        this.bio = bio;
        this.location = location;
        this.profilePhoto = profilePhoto;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
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
        return "Profile{" +
                "profileId=" + profileId +
                ", userId=" + userId +
                ", bio='" + bio + '\'' +
                ", location='" + location + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
