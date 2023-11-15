package dev.drugowick.timeseriespoc.controller.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @NotNull(message = "Heart rate (BPM) is mandatory")
    @Digits(fraction = 0, integer = 3, message = "Heart rate (BPM) should be lower than 1000")
    @Min(value = 1, message = "Heart rate (BPM) should be greater than 0")
    private Integer heartRate;
}
