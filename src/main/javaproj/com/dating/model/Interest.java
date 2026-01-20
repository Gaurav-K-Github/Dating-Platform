package com.dating.model;

/**
 * Interest model class representing user interests/hobbies
 */
public class Interest {
    private int interestId;
    private String interestName;

    // Constructors
    public Interest() {
    }

    public Interest(int interestId, String interestName) {
        this.interestId = interestId;
        this.interestName = interestName;
    }

    public Interest(String interestName) {
        this.interestName = interestName;
    }

    // Getters and Setters
    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "interestId=" + interestId +
                ", interestName='" + interestName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interest interest = (Interest) o;
        return interestId == interest.interestId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(interestId);
    }
}
