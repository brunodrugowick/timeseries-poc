package dev.drugowick.timeseriespoc.domain.repository;

import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementsRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findAllByUsernameAndCreatedDateAfter(String username, Long createdDateAfter);
    List<Measurement> findAllByCreatedDateAfterAndCreatedDateBefore(Long startDate, Long endDate);
}
