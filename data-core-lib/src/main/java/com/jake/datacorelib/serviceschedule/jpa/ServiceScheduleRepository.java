package com.jake.datacorelib.serviceschedule.jpa;

import com.jake.datacorelib.servicetype.jpa.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceScheduleRepository extends JpaRepository<ServiceSchedule, Long> {
}
