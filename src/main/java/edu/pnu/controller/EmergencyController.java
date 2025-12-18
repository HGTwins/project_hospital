package edu.pnu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.pnu.service.EmergencyService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EmergencyController {
	
	private final EmergencyService emergencyService;
	
	// 전체 검색 혹은 지역구로 검색
	@GetMapping("/ERInfo")
	public ResponseEntity<?> getERInfo(@RequestParam(required = false) String sigungu) {
		if (sigungu != null)
			return ResponseEntity.ok(emergencyService.getBySigungu(sigungu));
		return ResponseEntity.ok(emergencyService.getAll());
	}
}

