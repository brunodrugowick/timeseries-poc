package dev.drugowick.timeseriespoc.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Measurement implements Comparable<Measurement> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private Integer high;
    private Integer low;
    private Integer heartRate;

    @CreatedDate
    private Long createdDate;

    private String username;

    public List<Integer> asList() {
        return Arrays.asList(high, low, heartRate);
    }

    @Override
    public int compareTo(Measurement o) {
        return getCreatedDate().compareTo(o.getCreatedDate());
    }
}
