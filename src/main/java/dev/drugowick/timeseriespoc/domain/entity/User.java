package dev.drugowick.timeseriespoc.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
