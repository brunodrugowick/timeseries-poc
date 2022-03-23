package dev.drugowick.timeseriespoc.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SnapshotInput {

    private UUID uuid;

    @NotBlank(message = "Description is mandatory")
    @Length(max = 144, min = 1, message = "Description should have less than 144 characters")
    private String description;

    private Long startDate;
    private Long endDate;
    private boolean isPublic = false;
}
