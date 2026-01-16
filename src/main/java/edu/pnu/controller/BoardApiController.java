package edu.pnu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.BoardDto;
import edu.pnu.dto.BoardHospitalSearch;
import edu.pnu.dto.BoardMemberSearch;
import edu.pnu.service.BoardService;
import lombok.RequiredArgsConstructor;

// 후기 불러오기 컨트롤러

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardApiController {
	private final BoardService boardService;
	
	// 목록 조회 (페이징 포함)
	@GetMapping("/review")
	public ResponseEntity<?> getBoardList(@RequestParam(defaultValue = "0") Integer page,
	                                      @RequestParam(defaultValue = "10") Integer size) {
	    return ResponseEntity.ok(boardService.getBoards(page, size));
	}
	
	// 리뷰 등록
	@PostMapping("/review")
	public ResponseEntity<?> saveReview(@RequestBody BoardDto dto) {
	    try {
	        boardService.saveReview(dto);
	        return ResponseEntity.ok("리뷰가 등록되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("리뷰 등록 중 오류 발생: " + e.getMessage());
	    }
	}
	
	// 리뷰 수정
	@PutMapping("/review/{seq}")
	public ResponseEntity<?> updateReview(@PathVariable Long seq, @RequestBody BoardDto dto) {
	    try {
	        boardService.updateReview(seq, dto);
	        return ResponseEntity.ok("리뷰가 수정되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	    }
	}
	
	// 리뷰 삭제
	@DeleteMapping("/review/{seq}")
	public ResponseEntity<?> deleteReview(@PathVariable Long seq) {
	    try {
	        boardService.deleteReview(seq);
	        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
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