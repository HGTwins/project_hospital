package edu.pnu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.pnu.domain.board.Member;
import edu.pnu.domain.board.Role;
import edu.pnu.dto.MemberDuplicateDto;
import edu.pnu.dto.MemberJoinDto;
import edu.pnu.persistence.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepo;
	private final BCryptPasswordEncoder passwordEncoder;

	public Page<Member> getMembers(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return memberRepo.findAll(pageable);
	}

	public Member getMember(String username) {
		return memberRepo.findById(username).get();
	}
	
	// 회원 가입 시 중복 체크
	public void duplicateCheck(MemberDuplicateDto mdd) {
		if ("username".equals(mdd.getType())) {
			if (memberRepo.existsById(mdd.getValue())) {
				throw new RuntimeException("이미 존재하는 아이디입니다.");
			}
		} else if ("alias".equals(mdd.getType())) {
			if (memberRepo.existsByAlias(mdd.getValue())) {
				throw new RuntimeException("이미 존재하는 닉네임입니다.");
			}
		}
	}
	
	// 회원가입
	public void join(MemberJoinDto dto) {
		Member member = Member.builder().username(dto.getUsername()).password(passwordEncoder.encode(dto.getPassword()))
				.alias(dto.getAlias()).enabled(true).role(Role.ROLE_MEMBER).build();

		memberRepo.save(member);
	}

	// 멤버 삭제하기
	@Transactional
	public void deleteMember(String username) {
		// 삭제할 멤버 존재 확인
		Member member = memberRepo.findById(username)
				.orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

		// 현재 로그인한 사용자 권한 가져오기
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// 사용자의 권한(Role) 확인 (예: ROLE_ADMIN)
		boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		// 권한 체크: 관리자인 경우에만 삭제 허용
		if (isAdmin) {
			memberRepo.delete(member);
		} else {
			throw new RuntimeException("삭제 권한이 없습니다.");
		}
	}
}
