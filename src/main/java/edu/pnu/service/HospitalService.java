package edu.pnu.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.pnu.domain.hospital.BasicInfo;
import edu.pnu.persistence.hospital.BasicInfoRepository;
import edu.pnu.persistence.hospital.OffsetRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalService {
	private final BasicInfoRepository basicInfoRepo;
	private final OffsetRepository offsetRepo;
	
	// 시군구로 조회
	public List<BasicInfo> findBySigunguName(String sigunguName){
		return basicInfoRepo.findBySigunguName(sigunguName);
	}
	
	// 전부 조회
	public Page<BasicInfo> findAll(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return basicInfoRepo.findAll(pageable);
	}
	
	// 전체 병원 수
	public long countAllHospitals() {
		return basicInfoRepo.countAllHospitals();
	}
	
	// 시군구별 병원 수
	public long countHospitalsBySigunguName(String sigunguName) {
		return basicInfoRepo.countHospitalsBySigunguName(sigunguName);
	}

	// 위치로 조회
	public List<BasicInfo> findByLocation(double lon, double lat, int distance){
		return offsetRepo.findByLocation(lon, lat, distance);
	}
	
	/*
	// 상세 정보 조회
	// 모든 엔티티 들고 올 수 있음
	@Transactional(readOnly = true)
	public MedicalDetailDTO getMedicalDetail(String careEncCode) {
		MedicalInstitution institution = 
				miRepo.findById(careEncCode).orElseThrow(() -> new IllegalArgumentException("의료기관 없음"));
		return MedicalDetailDTO.builder()
				.institution(MedicalInstitutionDTO.from(institution))
				.departments(mdRepo.findByMedicalInstitution_CareEncCode(careEncCode)
						.stream().map(MedicalDepartmentDTO::from).toList())
				.operationInfos(moRepo.findByMedicalInstitution_CareEncCode(careEncCode)
						.stream().map(MedicalOperationInfoDTO::from).toList())
				.transports(mtRepo.findByMedicalInstitution_CareEncCode(careEncCode)
						.stream().map(MedicalTransportDTO::from).toList())
				.build();
	}
	*/
}
