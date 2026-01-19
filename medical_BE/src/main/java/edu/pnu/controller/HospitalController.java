package edu.pnu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 페이지(html) 처리 컨트롤러

@Controller
public class HospitalController {
	// 로그인
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	
	// oauth2 로그인
//	@GetMapping("/callback")
//	public String callbackPage() {
//		return "callback";
//	}
	
	// 시군구별 페이지
	@GetMapping("/medicalSigungu")
	public String medicalSigunguPage() {
		return "medicalSigungu";
	}
	
	// 위치별 페이지
	@GetMapping("/medicalInfo")
	public String medicalInfoPage() {
		return "medicalInfo";
	}
}
