package edu.pnu.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.pnu.dto.BasicInfoDTO;
import edu.pnu.dto.MedicalInfoSearch;
import edu.pnu.dto.MedicalSggSearch;
import edu.pnu.persistence.HospitalRepository;
import edu.pnu.persistence.SidoRepository;
import edu.pnu.persistence.SigunguRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalService {
	private final HospitalRepository hospitalRepo;
	private final SidoRepository sidoRepo;
	private final SigunguRepository sigunguRepo;
	
	// 시도명 프론트에 전달
	public List<String> findAllSidoNames() {
		return sidoRepo.findAllSidoNames();
	}
	
	// 시군구명 프론트에 전달
	public List<String> findSigunguNamesBySidoName(String sidoName) {
		return sigunguRepo.findSigunguNamesBySidoName(sidoName);
	}
	
	// 병원 조회
	public Page<BasicInfoDTO> findBySigungu(MedicalSggSearch mss) {
		Pageable pageable = PageRequest.of(mss.getPage(), mss.getSize());
		// 전체 병원
		if (mss.getSidoName() == null && mss.getSigunguName() == null) {
			return hospitalRepo.findAll(pageable).map(BasicInfoDTO::from);
		// 시도별 병원
		} else if (mss.getSidoName() != null && mss.getSigunguName() == null) {
			return hospitalRepo.findBySidoName(mss.getSidoName(), pageable)
					.map(BasicInfoDTO::from);
		// 시도 및 시군구별 병원
		} else {
			return hospitalRepo.findBySidoNameAndSigunguName(mss.getSidoName(), mss.getSigunguName(), pageable)
					.map(BasicInfoDTO::from);
		}
	}
	
	// 하나만 조회
	public BasicInfoDTO findById(Long hospitalId) {
		return hospitalRepo.findById(hospitalId)
				.map(BasicInfoDTO::from)
				.orElseThrow(() -> new IllegalArgumentException("해당 병원 없음"));
	}

	// 위치로 조회
	public List<BasicInfoDTO> findByLocation(MedicalInfoSearch mis){
	
		int limit = switch(mis.getLevel()) {
			case 1, 2 -> 1000;	// 전국 수준
			case 3, 4 -> 500;	// 시도
			default -> 200;		// 시군구
		};
		
		Pageable pageable = PageRequest.of(0, limit);

		return hospitalRepo.findByLocation(mis.getSwLat(), mis.getNeLat(), mis.getSwLng(), mis.getNeLng(), pageable)
				.stream()
				.map(BasicInfoDTO::from)
				.toList();
	}
	
	// 위치에 따른 병원 수
	public long countByLocation(MedicalInfoSearch mis) {
		if (mis.getLevel() <= 2) {
			// 전국 병원 수
			return hospitalRepo.count();
		} else {
			// 범위에 따른 병원 수
			return hospitalRepo.countByLocation(mis.getSwLat(), mis.getNeLat(), mis.getSwLng(), mis.getNeLng());
		}
	}
	
	// 전체 병원 유형
	
	// 시군구별 병원 유형
	
	// 전체 진료 과목
	
	// 시군구별 진료 과목
}
