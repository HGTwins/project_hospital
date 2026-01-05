package edu.pnu.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.MedicalInfoSearch;
import edu.pnu.dto.MedicalSggSearch;
import edu.pnu.service.HospitalService;
import edu.pnu.util.JWTUtil;
import lombok.RequiredArgsConstructor;

// 데이터 처리 컨트롤러

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HospitalApiController {
	private final HospitalService hospitalService;
	
	// oauth2 로그인 시 token 프론트에 전달
	@PostMapping("/jwtcallback")
	public ResponseEntity<?> apiCallback(@CookieValue String jwtToken) {
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, JWTUtil.prefix + jwtToken).build();
	}
	
	// 시도명 프론트에 전달
	@GetMapping("/sidoName")
	public ResponseEntity<?> sidoName() {
		return ResponseEntity.ok(hospitalService.findAllSidoNames());
	}
	
	// 시군구명 프론트에 전달
	@GetMapping("/sigunguName")
	public ResponseEntity<?> sigunguName(@RequestParam String sidoName) {
		return ResponseEntity.ok(hospitalService.findSigunguNamesBySidoName(sidoName));
	}
	
	// 시도 및 시군구명으로 병원 조회
	@GetMapping("/medicalSigungu")
	public ResponseEntity<?> medicalSigungu(MedicalSggSearch mss) {
		return ResponseEntity.ok(hospitalService.findBySigungu(mss));
	}

	// 위치로 병원 조회
	@GetMapping("/medicalInfo")
	public ResponseEntity<?> medicalInfo(MedicalInfoSearch mis) {
		return ResponseEntity.ok
					(Map.of(
						"list", hospitalService.findByLocation(mis),
						"totalElements", hospitalService.countByLocation(mis)
					));
	}

	// 하나만 조회
	@GetMapping("/medicalId")
	public ResponseEntity<?> medicalCareEncCode(@RequestParam Long hospitalId) {
		return ResponseEntity.ok(hospitalService.findById(hospitalId));
	}
	
	// 상세 페이지
	
}
