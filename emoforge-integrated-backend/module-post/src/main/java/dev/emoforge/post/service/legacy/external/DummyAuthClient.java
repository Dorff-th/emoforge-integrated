package dev.emoforge.post.service.legacy.external;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DummyAuthClient implements AuthClient {

    @Override
    public PublicProfileResponse getPublicMemberProfile(String uuid) {
        return null;
    }
}
