package dev.drugowick.timeseriespoc.service;

import dev.drugowick.timeseriespoc.domain.entity.Event;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserData {
    private long startDate;
    private long endDate;
    private Integer maxMeasurement;
    private List<Measurement> measurementList;
    private List<Event> eventList;
}
