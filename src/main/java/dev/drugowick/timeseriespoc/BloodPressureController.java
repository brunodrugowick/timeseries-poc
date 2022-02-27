package dev.drugowick.timeseriespoc;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BloodPressureController {

    private final BloodPressureRepository repository;

    BloodPressureController(BloodPressureRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/")
    public BloodPressure post(@RequestBody BloodPressureInput input) {
        // TODO Creating manually
        // Mapping here because cachaça
        BloodPressure bp = new BloodPressure();
        bp.setHigh(input.getHigh());
        bp.setLow(input.getLow());
        return repository.save(bp);
    }
}
