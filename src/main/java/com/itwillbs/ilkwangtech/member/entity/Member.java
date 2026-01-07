package com.itwillbs.ilkwangtech.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 20, nullable = false)
    @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "올바른 핸드폰 형식이 아닙니다")
    private String phoneNumber;

    @Column(length = 10, nullable = false)
    private String gender;

    @Column(length = 100, nullable = false, unique = true)
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @Column(length = 50)
    private String position;

    @Column(length = 50)
    private String department;

    @Column
    private LocalDate lastLogin;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Member() {
    }

}