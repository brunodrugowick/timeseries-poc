package dev.drugowick.timeseriespoc.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MeasurementInput {

    @NotNull(message = "High (mmHg) is mandatory")
    @Digits(fraction = 0, integer = 3, message = "High (mmHg) should be lower than 1000")
    @Min(value = 1, message = "High (mmHg) should be greater than 0")
    private Integer high;

    @NotNull(message = "Low (mmHg) is mandatory")
    @Digits(fraction = 0, integer = 3, message = "Low (mmHg) should be lower than 1000")
    @Min(value = 1, message = "Low (mmHg) should be greater than 0")
    private Integer low;

    @Digits(fraction = 0, integer = 3, message = "Heart rate (BPM) should be lower than 1000")
    @Min(value = 1, message = "Heart rate (BPM) should be greater than 0")
    private Integer heartRate;
}
