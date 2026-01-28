package dev.emoforge.attach.domain;

public enum AttachmentStatus {

    /** 임시 저장 상태 (Temporary) - Post 등록 전, cleanup 대상 */
    TEMP,

    /** 확정된 상태 (Confirmed) - Post 또는 Member에 매핑 완료 */
    CONFIRMED,

    /** 삭제된 상태 (Deleted) - 사용자 삭제 or 시스템 정리 */
    DELETED
}
