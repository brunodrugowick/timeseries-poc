package dev.drugowick.timeseriespoc.service.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Email
    private String email;

    private String providerId;
    private String provider;
    private boolean enabled;
    private String fullName;

}
