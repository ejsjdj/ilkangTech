package com.itwillbs.ilkwangtech.Hr.repository;

import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DraftRepository extends JpaRepository<DraftEntity, Long> {

    List<DraftEntity> findByMember_Id(Long id);

}
