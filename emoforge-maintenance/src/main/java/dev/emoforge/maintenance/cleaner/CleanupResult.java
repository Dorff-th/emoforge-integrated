package dev.emoforge.maintenance.cleaner;

public class CleanupResult {
    private int processed;
    private int deleted;
    private int skipped;
    private int failed;

    public void incProcessed() { processed++; }
    public void incDeleted() { deleted++; }
    public void incSkipped() { skipped++; }
    public void incFailed() { failed++; }

    public int getProcessed() { return processed; }
    public int getDeleted() { return deleted; }
    public int getSkipped() { return skipped; }
    public int getFailed() { return failed; }

    @Override
    public String toString() {
        return "CleanupResult{" +
                "processed=" + processed +
                ", deleted=" + deleted +
                ", skipped=" + skipped +
                ", failed=" + failed +
                '}';
    }
}