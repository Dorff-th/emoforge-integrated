package dev.emoforge.maintenance.cleaner;

public interface Cleaner {
    String name();
    CleanupResult run();
}