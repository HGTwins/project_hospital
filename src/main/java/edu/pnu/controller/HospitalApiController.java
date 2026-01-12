package edu.pnu.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.MedicalInfoSearch;
import edu.pnu.dto.MedicalPageSearch;
import edu.pnu.dto.MedicalChartSearch;
import edu.pnu.dto.MedicalCountSearch;
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
		return ResponseEntity.ok(hospitalService.getListAllSidoNames());
	}
	
	// 시군구명 프론트에 전달
	@GetMapping("/sigunguName")
	public ResponseEntity<?> sigunguName(@RequestParam String sidoName) {
		return ResponseEntity.ok(hospitalService.getListSigunguNamesBySidoName(sidoName));
	}
	
	// 시도 및 시군구명으로 병원 조회
	@GetMapping("/medicalInfo")
	public ResponseEntity<?> medicalSigungu(MedicalPageSearch mps) {
		return ResponseEntity.ok(hospitalService.getHospitalInfo(mps));
	}

	// 위치로 병원 조회
	@GetMapping("/medicalLocation")
	public ResponseEntity<?> medicalInfo(MedicalInfoSearch mis) {
		return ResponseEntity.ok(hospitalService.getListByLocation(mis));
	}

	// 하나만 조회
	@GetMapping("/medicalId")
	public ResponseEntity<?> medicalCareEncCode(@RequestParam Long hospitalId) {
		return ResponseEntity.ok(hospitalService.findById(hospitalId));
	}
	
	// 병원 수 (스코어 카드)
	@GetMapping("/medicalCountHospital")
	public ResponseEntity<?> medicalCountHospital(MedicalCountSearch mes) {
		return ResponseEntity.ok(hospitalService.getCountHospital(mes));
	}
	
	// 필수 의료 수 (스코어 카드)
	@GetMapping("/medicalEssential")
	public ResponseEntity<?> medicalEssential(MedicalPageSearch mps) {
		return ResponseEntity.ok(hospitalService.getPageEssential(mps));
	}
	
	// 일요일/공휴일 진료 병원 수 (스코어 카드)
	@GetMapping("/medicalHoliday")
	public ResponseEntity<?> medicalHoliday(MedicalPageSearch mps) {
		return ResponseEntity.ok(hospitalService.getPageHolidayOpen(mps));
	}
	
	// 야간 진료 병원 수 (스코어 카드)
	@GetMapping("/medicalNight")
	public ResponseEntity<?> medicalNight(MedicalPageSearch mps) {
		return ResponseEntity.ok(hospitalService.getPageNightOpen(mps));
	}
	
	// 병원 유형 수 (차트)
	@GetMapping("/medicalType")  
	public ResponseEntity<?> medicalType(MedicalChartSearch mcs) {
		return ResponseEntity.ok(hospitalService.getTopNWithOthersByType(mcs));
	}
		
	// 진료 과목 수 (차트)
	@GetMapping("/medicalDept")
	public ResponseEntity<?> medicalDept(MedicalChartSearch mcs) {
		return ResponseEntity.ok(hospitalService.getTopNWithOthersByDept(mcs));
	}
	
	// 상세 페이지
	@GetMapping("/medicalInfo/{hospitalId}")
	public ResponseEntity<?> medicalDetail(@PathVariable Long hospitalId) {
		return ResponseEntity.ok(hospitalService.getByHospitalId(hospitalId));
	}
}
