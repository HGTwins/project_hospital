package edu.pnu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import edu.pnu.domain.EmergencyLocation;
import edu.pnu.persistence.EmergencyLocationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmergencyService {

	private final EmergencyLocationRepository locationRepository;
	
	// 전체 검색
	public List<EmergencyLocation> getAll() {
		return locationRepository.findAll();
	}
	
	// 구로 검색
	public List<EmergencyLocation> getBySigungu(String gu) {
		return locationRepository.getBySigungu(gu);
	}
}
