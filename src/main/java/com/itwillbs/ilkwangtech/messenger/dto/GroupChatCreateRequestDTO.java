package com.itwillbs.ilkwangtech.messenger.dto;

import java.util.List;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupChatCreateRequestDTO {
    private String roomName;      // 방 이름
    private List<Long> memberIds; // 초대된 사원들의 ID 리스트
}