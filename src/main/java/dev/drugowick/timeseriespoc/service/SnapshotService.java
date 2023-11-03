package dev.drugowick.timeseriespoc.service;

import dev.drugowick.timeseriespoc.domain.entity.Snapshot;
import dev.drugowick.timeseriespoc.domain.repository.SnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SnapshotService {

    private static final Logger log = LoggerFactory.getLogger(SnapshotService.class);

    private final SnapshotRepository snapshotRepository;

    public SnapshotService(SnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    public List<Snapshot> findAllByUsername(String username) {
        log.info("Running DB search of Snapshots for {}", username);
        return snapshotRepository.findAllByUsername(username);
    }

    public Optional<Snapshot> getByUuid(UUID uuid) {
        log.info("Running DB search for Snapshot {}", uuid);
        return snapshotRepository.findById(uuid);
    }

    public Optional<Snapshot> getPublicSnapshot(UUID uuid) {
        log.info("Running DB search for Snapshot {}", uuid);
        return snapshotRepository.findByUuidAndIsPublic(uuid, true);
    }

    public Snapshot save(Snapshot snapshot) {
        return snapshotRepository.save(snapshot);
    }

}
