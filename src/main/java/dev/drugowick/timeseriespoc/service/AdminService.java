package dev.drugowick.timeseriespoc.service;

import dev.drugowick.timeseriespoc.domain.entity.MeasurementsCount;
import dev.drugowick.timeseriespoc.domain.repository.CustomSQLRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final CustomSQLRepository sqlRespository;

    public AdminService(CustomSQLRepository sqlRespository) {
        this.sqlRespository = sqlRespository;
    }

    public List<MeasurementsCount> getCounts() {
        return sqlRespository.getCountAll();
    }
}
