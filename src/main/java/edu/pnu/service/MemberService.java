package edu.pnu.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.pnu.domain.board.Member;
import edu.pnu.domain.board.Role;
import edu.pnu.dto.MemberJoinDto;
import edu.pnu.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepo;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public List<Member> getMembers() {
		return memberRepo.findAll();
	}
	
	public Member getMember(String username) {
		return memberRepo.findById(username).get();
	}
	
	public void join(MemberJoinDto dto) {
		// 아이디 중복 체크
		if (memberRepo.existsById(dto.getUsername())) {
			throw new RuntimeException("이미 존재하는 아이디입니다.");
		}
		
		// 엔티티 생성 및 저장
		Member member = Member.builder()
				.username(dto.getUsername())
				.password(passwordEncoder.encode(dto.getPassword()))
				.alias(dto.getAlias())
				.enabled(true)
				.role(Role.ROLE_MEMBER)
				.build();
		
		memberRepo.save(member);
	}
}
