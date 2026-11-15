package com.thiqa.backend.thiqa.backend.dto;

public record ScoreBreakdown(Integer score,
        int received,
        int prepaid,
        int delayed,
        int cancelled,
        int refused,
        int total) {

}
