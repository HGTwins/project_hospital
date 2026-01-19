package edu.pnu.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.OperationInfo;
import edu.pnu.dto.OperationInfoDto;

public interface OperationInfoRepository extends JpaRepository<OperationInfo, Long> {
	// 상세 페이지
	@Query("""
			SELECT new edu.pnu.dto.OperationInfoDto(b, o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			WHERE b.hospitalId = :hospitalId
			""")
	OperationInfoDto getByHospitalId(@Param("hospitalId") Long hospitalId);
	
	// 전체 일요일/공휴일 진료 병원
	@Query("""
			SELECT DISTINCT new edu.pnu.dto.OperationInfoDto(b, o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			WHERE o.closedHoliday IS NOT NULL OR o.closedSunday IS NOT NULL
			""")
	Page<OperationInfoDto> getPageAllByHolidayOpen(Pageable pageable);

	// 시도별 일요일/공휴일 진료 병원
	@Query("""
			SELECT DISTINCT new edu.pnu.dto.OperationInfoDto(b, o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			JOIN b.sidoCode sd
			WHERE sd.sidoName = :sidoName
			AND (o.closedHoliday IS NOT NULL OR o.closedSunday IS NOT NULL)
			""")
	Page<OperationInfoDto> getPageByHolidayOpenAndSidoName(
								@Param("sidoName") String sidoName, Pageable pageable);

	// 시군구별 일요일/공휴일 진료 병원
	@Query("""
			SELECT DISTINCT new edu.pnu.dto.OperationInfoDto(b, o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			JOIN b.sidoCode sd
			JOIN b.sigunguCode sg
			WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
			AND (o.closedHoliday IS NOT NULL OR o.closedSunday IS NOT NULL)
			""")
	Page<OperationInfoDto> getPageByHolidayOpenAndSidoNameAndSigunguName(
			@Param("sidoName") String sidoName,
			@Param("sigunguName") String sigunguName,
			Pageable pageable);

	// 전체 야간 진료 병원 수
	@Query("""
			SELECT DISTINCT new edu.pnu.dto.OperationInfoDto(b, o)
			FROM OperationInfo o 
			JOIN o.basicInfo b
			WHERE o.endMonday >= '18:01'
			OR o.endTuesday >= '18:01'
			OR o.endWednesday >= '18:01'
			OR o.endThursday >= '18:01'
			OR o.endFriday >= '18:01'
			OR o.endSaturday >= '18:01'
			OR o.endSunday >= '18:01'
			""")
	Page<OperationInfoDto> getPageAllByNightOpen(Pageable pageable);

	// 시도별 일요일/공휴일 진료 병원 수
	@Query("""
			SELECT DISTINCT new edu.pnu.dto.OperationInfoDto(b, o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			JOIN b.sidoCode sd
			WHERE sd.sidoName = :sidoName
			AND (o.endMonday >= '18:01'
			OR o.endTuesday >= '18:01'
			OR o.endWednesday >= '18:01'
			OR o.endThursday >= '18:01'
			OR o.endFriday >= '18:01'
			OR o.endSaturday >= '18:01'
			OR o.endSunday >= '18:01')
			""")
	Page<OperationInfoDto> getPageByNightOpenAndSidoName(
			@Param("sidoName") String sidoName, Pageable pageable);

	// 시군구별 일요일/공휴일 진료 병원 수
	@Query("""
			SELECT DISTINCT new edu.pnu.dto.OperationInfoDto(b, o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			JOIN b.sidoCode sd
			JOIN b.sigunguCode sg
			WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
			AND (o.endMonday >= '18:01'
			OR o.endTuesday >= '18:01'
			OR o.endWednesday >= '18:01'
			OR o.endThursday >= '18:01'
			OR o.endFriday >= '18:01'
			OR o.endSaturday >= '18:01'
			OR o.endSunday >= '18:01')
			""")
	Page<OperationInfoDto> getPageByNightOpenAndSidoNameAndSigunguName(
				@Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName,
				Pageable pageable);
}
