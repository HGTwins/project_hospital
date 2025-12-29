package edu.pnu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.pnu.domain.board.Member;
import edu.pnu.persistence.board.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepo;
	
	public List<Member> getMembers() {
		return memberRepo.findAll();
	}
	
	public Member getMember(String username) {
		return memberRepo.findById(username).get();
	}
}
