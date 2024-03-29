package dev.drugowick.timeseriespoc.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Event implements Comparable<Event> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    @CreatedDate
    private Long createdDate;

    private String username;

    @Override
    public int compareTo(Event o) {
        return getCreatedDate().compareTo(o.getCreatedDate());
    }
}
