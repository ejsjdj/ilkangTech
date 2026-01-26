package com.itwillbs.ilkwangtech.Hr.repository;

import com.itwillbs.ilkwangtech.Hr.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentRepostiory extends JpaRepository<AppointmentEntity, Long> {

}
