package com.thiqa.backend.thiqa.backend.controller;

import com.thiqa.backend.thiqa.backend.dto.ScoreBreakdown;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.service.ScoreService;
import com.thiqa.backend.thiqa.backend.shared.GlobalResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping
    public GlobalResponse<ScoreBreakdown> myScore(@AuthenticationPrincipal Customer customer) {
        return new GlobalResponse<>(scoreService.breakdown(customer));
    }
}