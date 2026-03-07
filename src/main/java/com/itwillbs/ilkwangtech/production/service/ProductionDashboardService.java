package com.itwillbs.ilkwangtech.production.service;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductionDashboardService {

    List<Object[]> getStatistics(String unit);
}
