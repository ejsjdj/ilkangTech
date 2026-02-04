package com.itwillbs.ilkwangtech.common.repository;

import com.itwillbs.ilkwangtech.common.entity.FileMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaRepository extends JpaRepository<FileMeta, Long> {
}
