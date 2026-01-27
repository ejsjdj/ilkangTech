package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.account.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    public List<Bank> findByIsActiveTrue();
}
