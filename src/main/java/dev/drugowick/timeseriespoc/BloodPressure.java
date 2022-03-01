package dev.drugowick.timeseriespoc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BloodPressure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @CreatedDate
    private Long createdDate;

    private String username;
}
