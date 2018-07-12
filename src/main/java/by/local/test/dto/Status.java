package by.local.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    PENDING("PENDING"),
    IN_PROGRESS("IN PROGRESS"),
    SKIPPED("SKIPPED"),
    FAILED("FAILED"),
    COMPLETED("COMPLETED");

    String value;
}
