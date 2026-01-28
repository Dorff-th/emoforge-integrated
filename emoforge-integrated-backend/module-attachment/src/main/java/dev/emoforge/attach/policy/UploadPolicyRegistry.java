package dev.emoforge.attach.policy;

import dev.emoforge.attach.domain.UploadType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UploadPolicyRegistry (Map 관리)
 */
@Component
@RequiredArgsConstructor
public class UploadPolicyRegistry {

    private final List<UploadPolicy> policies;
    private final Map<UploadType, UploadPolicy> policyMap = new HashMap<>();

    @PostConstruct
    void init() {
        for (UploadPolicy policy : policies) {
            policyMap.put(policy.getType(), policy);
        }
    }

    public UploadPolicy getPolicy(UploadType type) {
        return policyMap.get(type);
    }
}
