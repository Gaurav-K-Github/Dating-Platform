package com.dating.model;

import java.sql.Timestamp;

/**
 * Match model class representing matches between users
 */
public class Match {
    private int matchId;
    private int userId;
    private int targetUserId;
    private String matchStatus; // LIKED, PASSED, MATCHED
    private Timestamp createdAt;

    // Constructors
    public Match() {
    }

    public Match(int userId, int targetUserId, String matchStatus) {
        this.userId = userId;
        this.targetUserId = targetUserId;
        this.matchStatus = matchStatus;
    }

    public Match(int matchId, int userId, int targetUserId, String matchStatus, Timestamp createdAt) {
        this.matchId = matchId;
        this.userId = userId;
        this.targetUserId = targetUserId;
        this.matchStatus = matchStatus;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", userId=" + userId +
                ", targetUserId=" + targetUserId +
                ", matchStatus='" + matchStatus + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
