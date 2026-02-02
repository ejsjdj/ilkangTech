package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "INVOICES")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "INVOICES_SEQ_GENERATOR",
        sequenceName = "INVOICES_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICES_SEQ_GENERATOR")
    private Long invoiceId;

}
