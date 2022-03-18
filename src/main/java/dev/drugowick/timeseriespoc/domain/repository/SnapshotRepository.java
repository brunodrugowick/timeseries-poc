package dev.drugowick.timeseriespoc.domain.repository;

import dev.drugowick.timeseriespoc.domain.entity.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SnapshotRepository extends JpaRepository<Snapshot, UUID> {

    List<Snapshot> findAllByUsername(String username);
}
