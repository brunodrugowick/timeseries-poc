package dev.drugowick.timeseriespoc.service;

import dev.drugowick.timeseriespoc.domain.entity.Event;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class UserData {
    private Long startDate;
    private Long endDate;
    private Integer maxMeasurement;
    private Integer minMeasurement;
    private List<Measurement> measurementList;
    private List<Event> eventList;
}
