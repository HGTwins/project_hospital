package edu.pnu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.BoardHospitalSearch;
import edu.pnu.dto.BoardMemberSearch;
import edu.pnu.service.BoardService;
import lombok.RequiredArgsConstructor;

// 후기 불러오기 컨트롤러

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {
	private final BoardService boardService;
	
	// 게시글 id로 불러 오기
	@GetMapping("/review/seq")
	public ResponseEntity<?> getBoards(@RequestParam(required = false) Long seq) {
		if (seq == null) {
			return ResponseEntity.ok(boardService.getBoards()); 
		} else {
			return ResponseEntity.ok(boardService.getBoard(seq)); 
		}
	}
	
	// 멤버 id로 불러 오기
	@GetMapping("/review/memberId/{username}")
	public ResponseEntity<?> reviesMemberId(@PathVariable String username, BoardMemberSearch bms) {
		bms.setUsername(username);
		return ResponseEntity.ok(boardService.findByMemberUsername(bms)); 
	}
	
	// hospital id로 불러 오기
	@GetMapping("/review/hospitalId/{hospitalId}")
	public ResponseEntity<?> reviesHospitalId(@PathVariable Long hospitalId, BoardHospitalSearch bhs) {
		bhs.setHospitalId(hospitalId);
		return ResponseEntity.ok(boardService.findByBasicInfoHospitalId(bhs)); 
	}
}