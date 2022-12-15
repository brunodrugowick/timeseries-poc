package dev.drugowick.timeseriespoc.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"providerId"}), name = "users")
public class User {

    @Id
    private String email;

    private String providerId;
    private String provider;
    private boolean enabled;
    private String fullName;
}
