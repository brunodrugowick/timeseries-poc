package dev.drugowick.timeseriespoc.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MeasurementsCount {

    private String email;
    private Integer measurements;
    private Integer events;
    private Integer snapshots;
}
