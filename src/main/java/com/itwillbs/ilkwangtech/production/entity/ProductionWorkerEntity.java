package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "production_worker")
public class ProductionWorkerEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private ProcessEntity process;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}
