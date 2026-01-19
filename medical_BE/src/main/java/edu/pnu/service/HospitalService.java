package edu.pnu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.pnu.dto.HospitalDto;
import edu.pnu.domain.hospital.BasicInfo;
import edu.pnu.dto.DeptCountDto;
import edu.pnu.dto.EssentialHospitalDto;
import edu.pnu.dto.TypeCountDto;
import edu.pnu.dto.MedicalInfoSearch;
import edu.pnu.dto.MedicalPageSearch;
import edu.pnu.dto.OperationInfoDto;
import edu.pnu.dto.MedicalChartSearch;
import edu.pnu.dto.MedicalCountSearch;
import edu.pnu.persistence.DeptRepository;
import edu.pnu.persistence.OperationInfoRepository;
import edu.pnu.persistence.BasicInfoRepository;
import edu.pnu.persistence.SidoRepository;
import edu.pnu.persistence.SigunguRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalService {
	private final SidoRepository sidoRepo;
	private final SigunguRepository sigunguRepo;
	private final BasicInfoRepository basicRepo;
	private final DeptRepository deptRepo;
	private final OperationInfoRepository operRepo;
	
	// 시도명 프론트에 전달
	public List<String> getListAllSidoNames() {
		return sidoRepo.getListAllSidoNames();
	}
	
	// 시군구명 프론트에 전달
	public List<String> getListSigunguNamesBySidoName(String sidoName) {
		return sigunguRepo.getListSigunguNamesBySidoName(sidoName);
	}
	
	// 병원 조회
	public Page<HospitalDto> getHospitalInfo(MedicalPageSearch mps) {
		Pageable pageable = PageRequest.of(mps.getPage(), mps.getSize());
		// 전체 병원
		if (mps.getSidoName() == null && mps.getSigunguName() == null) {
			return basicRepo.findAll(pageable).map(HospitalDto::from);
		// 시도별 병원
		} else if (mps.getSidoName() != null && mps.getSigunguName() == null) {
			return basicRepo.findBySidoCodeSidoName(mps.getSidoName(), pageable)
					.map(HospitalDto::from);
		// 시도 및 시군구별 병원
		} else {
			return basicRepo.findBySidoCodeSidoNameAndSigunguCodeSigunguName(mps.getSidoName(), mps.getSigunguName(), pageable)
					.map(HospitalDto::from);
		}
	}
	
	// 하나만 조회
	public HospitalDto findById(Long hospitalId) {
		return basicRepo.findById(hospitalId)
				.map(HospitalDto::from)
				.orElseThrow(() -> new IllegalArgumentException("해당 병원 없음"));
	}

	// 위치로 조회
	public List<HospitalDto> getListByLocation(MedicalInfoSearch mis){
	
		int limit = switch(mis.getLevel()) {
			case 1, 2 -> 1000;	// 전국 수준
			case 3, 4 -> 500;	// 시도
			default -> 200;		// 시군구
		};
		
		Pageable pageable = PageRequest.of(0, limit);
		
		return basicRepo.getListByLocation(mis.getSwLat(), mis.getNeLat(), mis.getSwLng(), mis.getNeLng(), pageable)
				.stream()
				.map(HospitalDto::from)
				.toList();
	}
	
	// 병원 수 (스코어 카드)
	public Long getCountHospital(MedicalCountSearch mcs) {
		// 전체
		if (mcs.getSidoName() == null && mcs.getSigunguName() == null) {
			return basicRepo.count();
		// 시도별
		} else if (mcs.getSidoName() != null && mcs.getSigunguName() == null) {
			return basicRepo.countBySidoCodeSidoName(mcs.getSidoName());
		// 시군구별
		} else {
			return basicRepo.countBySidoCodeSidoNameAndSigunguCodeSigunguName(mcs.getSidoName(), mcs.getSigunguName());
		}
	}
	
	// 필수 의료 수 (스코어 카드)
	public Page<EssentialHospitalDto> getPageEssential(MedicalPageSearch mps){
		Pageable pageable = PageRequest.of(mps.getPage(), mps.getSize());
		Page<BasicInfo> essential;
		// 전체
		if (mps.getSidoName() == null && mps.getSigunguName() == null) {
			essential = deptRepo.getPageByAllEssential(pageable);
		// 시도별
		} else if (mps.getSidoName() != null && mps.getSigunguName() == null) {
			essential = deptRepo.getPageByEssentialAndSidoName(mps.getSidoName(), pageable);
		// 시군구별
		} else {
			essential = deptRepo.getPageByEssentialAndSidoNameAndSigunguName(
							mps.getSidoName(), mps.getSigunguName(), pageable);
		}
		// DTO 변환 및 과목 합치기
	    return essential.map(hospital -> {
	        // 이 병원의 필수 과목 리스트를 가져와서 문자열로 합침 
	        String deptNames = hospital.getDeptDoctors().stream()
	            .filter(dd -> List.of("10", "11", "24").contains(dd.getDeptCode().getDeptCode()))
	            .map(dd -> dd.getDeptCode().getDeptName())
	            .collect(Collectors.joining(", "));
	        
	        return new EssentialHospitalDto(hospital, deptNames);
	    });
	}
	
	// 일요일/공휴일 진료 병원 수 (스코어 카드)
	public Page<OperationInfoDto> getPageHolidayOpen(MedicalPageSearch mps) {
		Pageable pageable = PageRequest.of(mps.getPage(), mps.getSize());
		// 전체
		if (mps.getSidoName() == null && mps.getSigunguName() == null) {
			return operRepo.getPageAllByHolidayOpen(pageable);
		// 시도별
		} else if (mps.getSidoName() != null && mps.getSigunguName() == null) {
			return operRepo.getPageByHolidayOpenAndSidoName(mps.getSidoName(), pageable);
		// 시군구별
		} else {
			return operRepo.getPageByHolidayOpenAndSidoNameAndSigunguName(
					mps.getSidoName(), mps.getSigunguName(), pageable);
		}
	}
	
	// 야간 진료 병원 수 (스코어 카드)
	public Page<OperationInfoDto> getPageNightOpen(MedicalPageSearch mps) {
		Pageable pageable = PageRequest.of(mps.getPage(), mps.getSize());
		// 전체
		if (mps.getSidoName() == null && mps.getSigunguName() == null) {
			return operRepo.getPageAllByNightOpen(pageable);
		// 시도별
		} else if (mps.getSidoName() != null && mps.getSigunguName() == null) {
			return operRepo.getPageByNightOpenAndSidoName(mps.getSidoName(), pageable);
		// 시군구별
		} else {
			return operRepo.getPageByNightOpenAndSidoNameAndSigunguName(
					mps.getSidoName(), mps.getSigunguName(), pageable);
		}
	}
	
	// 병원 유형 수 (차트)
	public List<TypeCountDto> getTopNWithOthersByType(MedicalChartSearch mcs) {
		List<TypeCountDto> typeList;
		// 전체
		if (mcs.getSidoName() == null && mcs.getSigunguName() == null) {
			typeList =  basicRepo.getListByType();
		// 시도별
		} else if (mcs.getSidoName() != null && mcs.getSigunguName() == null) {
			typeList =  basicRepo.getListByTypeAndSidoName(mcs.getSidoName());
		// 시군구별
		} else {
			typeList =  basicRepo.getListByTypeAndSidoNameAndSigunguName(mcs.getSidoName(), mcs.getSigunguName());
		}
		
		// 1. count 기준 모든 유형 내림차순 정렬
		typeList.sort((a, b) -> b.getCount().compareTo(a.getCount()));
		
		// 2. 상위 N개
		List<TypeCountDto> topList =  typeList.stream().limit(mcs.getTopN()).toList();
		
		// 3. 기타 합산
		long othersCount = typeList.stream().skip(mcs.getTopN()).mapToLong(TypeCountDto::getCount).sum();
		
		// 4. 기타 항목 있으면 topList에 추가
		if (othersCount > 0) {
			List<TypeCountDto> result = new ArrayList<>(topList);
			result.add(new TypeCountDto("기타", othersCount));
			return result;
		}
		
		return topList;
	}
	
	// 진료 과목 수 (차트)
	public List<DeptCountDto> getTopNWithOthersByDept(MedicalChartSearch mcs) {
		List<DeptCountDto> deptList;
		// 전체
		if (mcs.getSidoName() == null && mcs.getSigunguName() == null) {
			deptList =  deptRepo.getListByDept();
		// 시도별
		} else if (mcs.getSidoName() != null && mcs.getSigunguName() == null) {
			deptList =  deptRepo.getListByDeptAndSidoName(mcs.getSidoName());
		// 시군구별
		} else {
			deptList = deptRepo.getListByDeptAndSidoNameAndSigunguName(mcs.getSidoName(), mcs.getSigunguName());
		}
		
		deptList.sort((a, b) -> b.getCount().compareTo(a.getCount()));			
		List<DeptCountDto> topList =  deptList.stream().limit(mcs.getTopN()).toList();
		long othersCount = deptList.stream().skip(mcs.getTopN()).mapToLong(DeptCountDto::getCount).sum();

		if (othersCount > 0) {
			List<DeptCountDto> result = new ArrayList<>(topList);
			result.add(new DeptCountDto("기타", othersCount));
			return result;
		}
		
		return topList;
	}
	
	// 상세 페이지
	public OperationInfoDto getByHospitalId(Long hospitalId) {
		return operRepo.getByHospitalId(hospitalId);
	}
}
