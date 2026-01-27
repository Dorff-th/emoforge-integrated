package dev.emoforge.auth.enums;

public enum MemberStatus {
    ACTIVE("활성"),
    INACTIVE("차단");

    private final String description;

    MemberStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
