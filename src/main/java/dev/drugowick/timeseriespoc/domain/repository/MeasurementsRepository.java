package dev.drugowick.timeseriespoc.domain.repository;

import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MeasurementsRepository extends JpaRepository<Measurement, UUID> {

    List<Measurement> findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(String username, Long createdDateAfter, Long createdDateBefore);
}
