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
	    return messengerRepository.findMemberDeptRows().stream()
	        .map(r -> new MemberDeptRowDTO((String) r[0], (String) r[1]))
	        .toList();
	}
}
