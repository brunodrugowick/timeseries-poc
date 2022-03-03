package dev.drugowick.timeseriespoc.domain.repository;

import dev.drugowick.timeseriespoc.domain.entity.BloodPressure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodPressureRepository extends JpaRepository<BloodPressure, Long> {

    List<BloodPressure> findAllByUsername(String username);
}
