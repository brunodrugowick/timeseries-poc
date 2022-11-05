package dev.drugowick.timeseriespoc.domain.repository;

import dev.drugowick.timeseriespoc.domain.entity.MeasurementsCount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomSQLRepository {

    public static final String EMAIL_COLUMN = "email";
    public static final String MEASUREMENTS_COUNT_COLUMN = "measurements";
    public static final String EVENTS_COUNT_COLUMN = "events";
    public static final String SNAPSHOTS_COUNT_COLUMN = "snapshots";
    private final JdbcTemplate jdbcTemplate;

    public CustomSQLRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static final String COUNTALLSQL = """
                SELECT
                    U.email as email,
                    COUNT(distinct M.id) as measurements,
                    COUNT(distinct E.id) AS events,
                    COUNT(distinct S.uuid) AS snapshots
                FROM MEASUREMENT M
                    LEFT JOIN USERS U
                        ON M.username = U.provider_id
                    LEFT JOIN EVENT E
                        ON M.username = E.username
                    LEFT JOIN SNAPSHOT S
                        ON M.username = S.username
                WHERE
                    U.email IS NOT NULL
                GROUP BY U.email
            """;

    public List<MeasurementsCount> getCountAll() {
        List<MeasurementsCount> measurementsCounts = new ArrayList<>();
        jdbcTemplate.queryForList(COUNTALLSQL).forEach(line -> measurementsCounts.add(new MeasurementsCount(
                line.get(EMAIL_COLUMN).toString(),
                Integer.parseInt(line.get(MEASUREMENTS_COUNT_COLUMN).toString()),
                Integer.parseInt(line.get(EVENTS_COUNT_COLUMN).toString()),
                Integer.parseInt(line.get(SNAPSHOTS_COUNT_COLUMN).toString())
        )));
        return measurementsCounts;
    }
}
