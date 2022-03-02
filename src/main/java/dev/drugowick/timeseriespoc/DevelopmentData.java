package dev.drugowick.timeseriespoc;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Profile("personal || development")
public class DevelopmentData {

    private final String DUMMY_USERNAME = "developer";

    private final BloodPressureRepository repository;

    public DevelopmentData(BloodPressureRepository repository) {
        this.repository = repository;

        System.out.println("Adding development data.");
        addData();
    }

    private void addData() {
        for (int i = 0; i < 30; i++) {
            Random r = new Random();

            var bp = new BloodPressure();
            bp.setHigh(ThreadLocalRandom.current().nextInt(120, 199));
            bp.setLow(ThreadLocalRandom.current().nextInt(60, 110));
            bp.setHeartRate(ThreadLocalRandom.current().nextInt(40, 110));
            bp.setUsername(DUMMY_USERNAME);

            repository.save(bp);
        }
    }
}
