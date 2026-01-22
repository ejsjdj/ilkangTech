package com.itwillbs.ilkwangtech.messenger.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "chatRoom")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chatRoomSeq")
	@SequenceGenerator(
	    name = "chatRoomSeq",
	    sequenceName = "CHATROOM_SEQ",
	    allocationSize = 1
	)
	private Long id;


    @Column(name = "roomType", nullable = false, length = 10)
    private String roomType;

    @Column(name = "roomName", length = 100)
    private String roomName;

    @Column(name = "createdBy", nullable = false)
    private Long createdBy;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "direct_emp1") 
    private Long directEmp1;

    @Column(name = "direct_emp2")
    private Long directEmp2;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}


