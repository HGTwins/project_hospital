package edu.pnu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.pnu.dto.MemberJoinDto;
import edu.pnu.service.MemberService;
import lombok.RequiredArgsConstructor;

// 회원가입 및 멤버 전용 컨트롤러

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {
	
	private final MemberService memberService;
	
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody MemberJoinDto dto){
		memberService.join(dto);
		return ResponseEntity.ok("회원가입이 완료되었습니다.");
	}
}
