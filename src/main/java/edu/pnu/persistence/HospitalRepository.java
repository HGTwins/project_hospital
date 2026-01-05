package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.BasicInfo;
import edu.pnu.dto.HospitalTypeCountDTO;

public interface HospitalRepository extends JpaRepository<BasicInfo, Long> {
	// 시도명으로 조회
	@Query("""
		    SELECT b
		    FROM BasicInfo b
		    JOIN b.sidoCode sd
		    WHERE sd.sidoName = :sidoName
		""")
	Page<BasicInfo> findBySidoName(@Param("sidoName") String sidoName, Pageable pageable);
	
	// 시도명 및 시군구명으로 조회
	@Query("""
		    SELECT b
		    FROM BasicInfo b
		    JOIN b.sidoCode sd
		    JOIN b.sigunguCode sg
		    WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
		""")
	Page<BasicInfo> findBySidoNameAndSigunguName(@Param("sidoName") String sidoName,
												@Param("sigunguName") String sigunguName,
												Pageable pageable);
	
	// 위치로 조회
	@Query("""
			SELECT b
			FROM BasicInfo b
            JOIN b.offset o
            WHERE o.latitude BETWEEN :swLat AND :neLat
            AND o.longitude BETWEEN :swLng AND :neLng
            ORDER BY function('RAND')
		""")
    List<BasicInfo> findByLocation(@Param("swLat") double swLat, @Param("neLat") double neLat,
    								@Param("swLng") double swLng, @Param("neLng") double neLng,
    								Pageable pageable);
	
	// 위치에 따른 병원 수
	@Query("""
			SELECT COUNT(b)
			FROM BasicInfo b
            JOIN b.offset o
            WHERE o.latitude BETWEEN :swLat AND :neLat
            AND o.longitude BETWEEN :swLng AND :neLng
			""")
	long countByLocation(@Param("swLat") double swLat, @Param("neLat") double neLat,
						@Param("swLng") double swLng, @Param("neLng") double neLng);
	
	// 전체 병원 유형
	@Query("""
			SELECT new edu.pnu.dto.HospitalTypeCountDTO(t.typeName, COUNT(b))
			FROM BasicInfo b
			JOIN b.typeCode t
			GROUP BY t.typeName
		""")
	List<HospitalTypeCountDTO> countAllByType();
	
	// 시군구별 병원 유형
	@Query("""
			SELECT new edu.pnu.dto.HospitalTypeCountDTO(t.typeName, COUNT(b))
			FROM BasicInfo b
			JOIN b.typeCode t
			JOIN b.sidoCode sd
			JOIN b.sigunguCode sg
			WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
			GROUP BY t.typeName
		""")
	List<HospitalTypeCountDTO> countByTypeAndSidoNameAndSigunguName(@Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName);
	
	// 전체 진료 과목
	
	// 시군구별 진료 과목
	
	// 전체 필수 의료(산부인과, 응급의학과, 소아청소년과) 개수
	
	// 시군구별 필수 의료(산부인과, 응급의학과, 소아청소년과) 개수
	
	// 전체 일요일/공휴일 진료 병원 수

	// 시군구별 일요일/공휴일 진료 병원 수
}