package com.itwillbs.ilkwangtech.messenger.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.messenger.dto.MemberDeptRowDTO;
import com.itwillbs.ilkwangtech.messenger.repository.MessengerRepository;

@Service
public class MessengerService {

	private final MessengerRepository messengerRepository;
	
	public MessengerService(MessengerRepository messengerRepository) {
		this.messengerRepository = messengerRepository;
	}
	
	public List<MemberDeptRowDTO> getMemberDeptRows() {
	    List<Object[]> rows = messengerRepository.findMemberDeptRows();

	    return rows.stream()
	        .map(r -> new MemberDeptRowDTO(
	            ((Number) r[0]).longValue(), // memberId
	            (String) r[1],              // memberName
	            (String) r[2]               // departmentName
	        ))
	        .toList();
	}
}
