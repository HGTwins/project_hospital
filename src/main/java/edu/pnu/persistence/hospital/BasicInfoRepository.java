package edu.pnu.persistence.hospital;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.BasicInfo;
import edu.pnu.dto.HospitalTypeCountDTO;

public interface BasicInfoRepository extends JpaRepository<BasicInfo, String> {
	
	// 시도명 및 시군구명으로 조회
	@Query("SELECT b FROM BasicInfo b " +
		   "JOIN SigunguCode s ON b.sidoCode = s.sidoCode " +
		   "WHERE s.sigunguName = :sigunguName")
	List<BasicInfo> findBySigunguName(@Param("sigunguName") String sigunguName);
	
	// 전체 병원 수
	@Query("SELECT COUNT(b) FROM BasicInfo b")
	long countAllHospitals();
	
	// 시군구별 병원 수 
	@Query("SELECT COUNT(b) FROM BasicInfo b " +
			"JOIN SigunguCode s ON b.sidoCode = s.sidoCode " +
			"WHERE s.sigunguName = :sigunguName")
	long countHospitalsBySigunguName(@Param("sigunguName") String sigunguName);
	
	// 전체 병원 유형
	@Query("SELECT new edu.pnu.dto.HospitalTypeCountDTO(t.typeCode, COUNT(b))"
			+ "FROM BasicInfo b " 
			+ "JOIN b.typeCode t GROUP BY t.typeCode")
	List<HospitalTypeCountDTO> countAllByType();
	
	// 시군구별 병원 유형
	@Query("SELECT new edu.pnu.dto.HospitalTypeCountDTO(t.typeCode, COUNT(b))"
			+ "FROM BasicInfo b " 
			+ "JOIN b.typeCode t "
			+ "JOIN SigunguCode s ON b.sidoCode = s.sidoCode "
			+ "WHERE s.sigunguName = :sigunguName "
			+ "GROUP BY t.typeCode")
	List<HospitalTypeCountDTO> countByTypeAndSigunguName(@Param("sigunguName") String sigunguName);
}