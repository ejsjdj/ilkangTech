package com.itwillbs.ilkwangtech.hr.repository;

import com.itwillbs.ilkwangtech.hr.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentRepostiory extends JpaRepository<AppointmentEntity, Long> {

}
