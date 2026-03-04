package dev.emoforge.maintenance.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cleanup")
public class CleanupProperties {

    /**
     * TEMP 파일 삭제 유예기간 (예: 6h, 1m)
     */
    private Duration gracePeriod = Duration.ofHours(6);

    /**
     * 실제 삭제 없이 로그만 남김
     */
    private boolean dryRun = true;

    /**
     * 배치 처리 크기
     */
    private int batchSize = 200;

    public Duration getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Duration gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}