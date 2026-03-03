package com.itwillbs.ilkwangtech.sales.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "purchase_return")
public class PurchaseReturnEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_line_id")
    private PurchaseRequestEntity header;

    @Column(name = "return_qty")
    private Long returnQty;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "memo")
    private String memo;

    @Column(name = "return_date")
    private LocalDate returnDate;

    public static PurchaseReturnEntity create(PurchaseRequestEntity header,
                                              Long returnQty,
                                              Member member,
                                              String memo){

        PurchaseReturnEntity entity = new PurchaseReturnEntity();

        entity.header = header;
        entity.returnQty = returnQty;
        entity.status = "READY";
        entity.member = member;
        entity.returnDate = LocalDate.now();
        entity.memo = memo;


        return entity;
    }
}
