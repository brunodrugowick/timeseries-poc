package dev.drugowick.timeseriespoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TimeseriesPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeseriesPocApplication.class, args);
    }

}
