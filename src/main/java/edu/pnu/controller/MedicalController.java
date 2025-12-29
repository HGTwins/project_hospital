package edu.pnu.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import edu.pnu.service.HospitalService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MedicalController {
	private final HospitalService hospitalService;
	
	// index
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	// 시군구로 조회
	@GetMapping("/medicalSigungu")
	public ResponseEntity<?> medicalDetails(@RequestParam(required = false) String sigunguName,
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
		if (sigunguName != null) {
			return ResponseEntity.ok(
				Map.of(
						"list", hospitalService.findBySigunguName(sigunguName),
						"totalCount", hospitalService.countAllHospitals(),
						"sigunguCount", hospitalService.countHospitalsBySigunguName(sigunguName)
	            ));	
		}
		return ResponseEntity.ok(
			Map.of(
	            "list", hospitalService.findAll(page, size),
	            "totalCount", hospitalService.countAllHospitals()
	        ));	
	}
	
	// 위치로 조회
	@GetMapping("/medicalInfo")
	public ResponseEntity<?> medicalInfo(@RequestParam(defaultValue = "129") double lon,
			@RequestParam(defaultValue = "35") double lat) {
		return ResponseEntity.ok(hospitalService.findByLocation(lon, lat, 10));
	}
	
	
	/*
	// 상세 정보 조회
	@GetMapping("/medicalInfo/{careEncCode}")
	public ResponseEntity<?> medicalInfo(@PathVariable String careEncCode) {
		return ResponseEntity.ok(mService.getMedicalDetail(careEncCode));
	}

	*/
}
