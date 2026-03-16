package dev.emoforge.core.security.service;

/**
 * Security 계층에서 회원 상태 조회를 위해 사용하는 포트 인터페이스.
 * core 모듈이 auth 도메인에 직접 의존하지 않도록 하기 위한 추상화 역할을 한다.
 */
public interface MemberSecurityReader {
    boolean isActive(String uuid);
}
