package dev.drugowick.timeseriespoc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodPressureRepository extends JpaRepository<BloodPressure, Long> {

    List<BloodPressure> findAllByUsername(String username);
}
