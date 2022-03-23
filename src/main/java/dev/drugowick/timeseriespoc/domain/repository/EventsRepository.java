package dev.drugowick.timeseriespoc.domain.repository;

import dev.drugowick.timeseriespoc.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventsRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(String username, Long createdDateAfter, Long createdDateBefore);
    List<Event> findAllByCreatedDateAfterAndCreatedDateBefore(Long startDate, Long endDate);
}
