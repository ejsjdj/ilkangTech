package com.itwillbs.ilkwangtech.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "genders")
@ToString
public class Gender {
    // 성벌 1남자 2 여자
    @Id
    int id;
    String gender;

}
