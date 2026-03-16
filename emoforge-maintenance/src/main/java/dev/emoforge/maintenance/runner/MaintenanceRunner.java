package dev.emoforge.maintenance.runner;

import dev.emoforge.maintenance.cleaner.AttachmentCleaner;
import dev.emoforge.maintenance.cleaner.EditorCleaner;
import dev.emoforge.maintenance.cleaner.FileSystemCleaner;
import dev.emoforge.maintenance.cleaner.ProfileCleaner;
import dev.emoforge.maintenance.config.CleanupProperties;
import java.time.LocalDateTime;

import dev.emoforge.maintenance.notify.DiscordNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceRunner implements ApplicationRunner {

    private final CleanupProperties props;
    private final EditorCleaner editorCleaner;
    private final ProfileCleaner profileCleaner;
    private final AttachmentCleaner attachmentCleaner;
    private final FileSystemCleaner fileSystemCleaner;
    //2026.03.16 추가
    private final DiscordNotifier notifier;

    @Override
    public void run(ApplicationArguments args) {
        String mode = getOption(args, "mode", "all").toLowerCase();

        // 실행 정보 요약 (로그 시스템 붙이기 전이라도 콘솔로)
        System.out.println("========================================");
        System.out.println("[MAINTENANCE] Started: " + LocalDateTime.now());
        System.out.println("[MAINTENANCE] Mode: " + mode);
        System.out.println("[MAINTENANCE] GracePeriod: " + props.getGracePeriod());
        System.out.println("[MAINTENANCE] DryRun: " + props.isDryRun());
        System.out.println("[MAINTENANCE] BatchSize: " + props.getBatchSize());
        System.out.println("========================================");

        notifier.send(
                "🚀 Emoforge maintenance started\n" +
                        "mode=" + mode +
                        "\nstarted=" + LocalDateTime.now()
        );

        try {

            switch (mode) {
                case "profile" -> profileCleaner.run();
                case "editor" -> editorCleaner.run();
                case "attachment" -> attachmentCleaner.run();
                case "filesystem" -> fileSystemCleaner.run();
                case "all" -> {
                    editorCleaner.run();
                    profileCleaner.run();
                    attachmentCleaner.run();
                    fileSystemCleaner.run();
                }
                default -> {
                    System.out.println("❌ Invalid mode: " + mode);
                    System.out.println("✅ Usage: --mode=profile|editor|attachment|all");
                    // 배치 프로그램이면 실패코드로 종료하는게 깔끔
                    System.exit(1);
                }
            }

        } catch (Exception e) {
            notifier.send(
                    "🔥 Emoforge maintenance FAILED\n" + e.getMessage()
            );

            throw e;
        }

        notifier.send(
                "🧹 Emoforge maintenance success\n" +
                        "mode=" + mode +
                        "\nfinished=" + LocalDateTime.now()
        );

        System.out.println("========================================");
        System.out.println("[MAINTENANCE] Finished: " + LocalDateTime.now());
        System.out.println("========================================");

        // 배치는 보통 여기서 끝나야 하니 강제 종료(선택)
        // Spring이 남아있는 non-daemon thread 없으면 자동 종료되긴 하는데,
        // 안전하게 끊고 싶으면 아래를 쓴다.
        System.exit(0);
    }

    private void runProfile() {
        System.out.println("[RUN] profile cleanup (stub)");
        // TODO: ProfileCleaner 호출
    }

    private void runEditor() {
        System.out.println("[RUN] editor cleanup (stub)");
        // TODO: EditorCleaner 호출
    }

    private void runAttachment() {
        System.out.println("[RUN] attachment cleanup (stub)");
        // TODO: AttachmentCleaner 호출
    }

    private String getOption(ApplicationArguments args, String key, String defaultValue) {
        if (!args.containsOption(key)) return defaultValue;
        var values = args.getOptionValues(key);
        if (values == null || values.isEmpty()) return defaultValue;
        return values.get(0);
    }
}