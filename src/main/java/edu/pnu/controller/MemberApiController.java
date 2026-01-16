package edu.pnu.controller;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.pnu.dto.MemberDuplicateDto;
import edu.pnu.dto.MemberJoinDto;
import edu.pnu.service.MemberService;
import lombok.RequiredArgsConstructor;

// 회원가입 및 멤버 전용 컨트롤러

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberService memberService;

	// 관리자용 모든 멤버 불러오기
	@GetMapping("/admin/getMembers")
	public ResponseEntity<?> getMembers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(memberService.getMembers(page, size));
	}

	// 관리자용 특정 멤버 삭제
	@DeleteMapping("/admin/getMember/{username}")
	public ResponseEntity<?> deleteMember(@PathVariable String username) {
		try {
			memberService.deleteMember(username);
			return ResponseEntity.ok("멤버가 삭제되었습니다.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
		}
	}

	// 특정 멤버 불러오기
	@GetMapping("/getMember/{username}")
	public ResponseEntity<?> getMember(@PathVariable String username) {
		return ResponseEntity.ok(memberService.getMember(username));
	}
	
	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody MemberJoinDto dto) {
		memberService.join(dto);
		return ResponseEntity.ok("회원가입이 완료되었습니다.");
	}

	// 중복 확인
	@GetMapping("/check-duplicate")
	public ResponseEntity<?> duplicateCheck(MemberDuplicateDto mdd) {
		try {
			memberService.duplicateCheck(mdd);
			// 중복이 없을 때 보내는 메시지
			return ResponseEntity.ok(Collections.singletonMap("message", "사용 가능한 " + mdd.getType() + "입니다."));
		} catch (RuntimeException e) {
			// 중복이 있을 때
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("message", e.getMessage()));
		}
	}
}
