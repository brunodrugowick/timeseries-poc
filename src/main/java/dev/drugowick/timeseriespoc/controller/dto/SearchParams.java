package dev.drugowick.timeseriespoc.controller.dto;

import lombok.Data;

@Data
public class SearchParams {
    private long daysOffset = 3;
    private long startDate;
    private long endDate;
}
