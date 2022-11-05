package dev.drugowick.timeseriespoc.domain.repository;

import dev.drugowick.timeseriespoc.domain.entity.MeasurementCount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomSQLRepository {

    public static final String MEASUREMENTCOUNTSQL = """
        SELECT U.email as email, COUNT(M.id) as count
        FROM MEASUREMENT M
                 LEFT JOIN USERS U
                           ON M.username = U.provider_id
        WHERE U.email IS NOT NULL
        GROUP BY U.email
        """;

    private final JdbcTemplate jdbcTemplate;

    public CustomSQLRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MeasurementCount> countByUsername() {
        List<MeasurementCount> measurementCounts = new ArrayList<>();
        jdbcTemplate.queryForList(MEASUREMENTCOUNTSQL).forEach(line -> measurementCounts.add(new MeasurementCount(
                line.get("email").toString(),
                Integer.parseInt(line.get("count").toString())
        )));
        return measurementCounts;
    }
}
