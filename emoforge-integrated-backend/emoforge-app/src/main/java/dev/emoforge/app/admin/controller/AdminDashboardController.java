package dev.emoforge.app.admin.controller;

import dev.emoforge.app.admin.dto.DashboardDTO;
import dev.emoforge.app.admin.service.AdminDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Dashboard API", description = "Admin dashboard statistics API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @Operation(summary = "Get admin dashboard statistics")
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboardStatistics() {
        return ResponseEntity.ok(adminDashboardService.getDashboardStatistics());
    }
}
