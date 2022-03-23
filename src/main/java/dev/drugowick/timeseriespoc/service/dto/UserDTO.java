package dev.drugowick.timeseriespoc.service.dto;

import lombok.*;

import javax.validation.constraints.Email;

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
