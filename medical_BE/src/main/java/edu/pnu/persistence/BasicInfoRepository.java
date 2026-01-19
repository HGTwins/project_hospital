package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.BasicInfo;
import edu.pnu.dto.TypeCountDto;

public interface BasicInfoRepository extends JpaRepository<BasicInfo, Long> {
	// 시도명으로 조회
	Page<BasicInfo> findBySidoCodeSidoName(@Param("sidoName") String sidoName, Pageable pageable);
	
	// 시도명 및 시군구명으로 조회
	Page<BasicInfo> findBySidoCodeSidoNameAndSigunguCodeSigunguName(@Param("sidoName") String sidoName,
												@Param("sigunguName") String sigunguName, Pageable pageable);
	// 시도별 병원 수
	Long countBySidoCodeSidoName(@Param("sidoName") String sidoName);
		
	// 시군구별 병원 수
	Long countBySidoCodeSidoNameAndSigunguCodeSigunguName(
				@Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName);
	
	// 위치로 조회
	@Query("""
			SELECT b
			FROM BasicInfo b
            JOIN b.offset o
            WHERE o.latitude BETWEEN :swLat AND :neLat
            AND o.longitude BETWEEN :swLng AND :neLng
            ORDER BY FUNCTION('MD5', b.hospitalId)
		""")
    List<BasicInfo> getListByLocation(@Param("swLat") double swLat, @Param("neLat") double neLat,
    								@Param("swLng") double swLng, @Param("neLng") double neLng,
    								Pageable pageable);
	
	// 전체 병원 유형 수
	@Query("""
			SELECT new edu.pnu.dto.TypeCountDto(t.typeName, COUNT(b))
			FROM BasicInfo b
			JOIN b.typeCode t
			GROUP BY t.typeName
		""")
	List<TypeCountDto> getListByType();
	
	
	// 시도별 병원 유형 수
	@Query("""
			SELECT new edu.pnu.dto.TypeCountDto(t.typeName, COUNT(b))
			FROM BasicInfo b
			JOIN b.typeCode t
			JOIN b.sidoCode sd
			WHERE sd.sidoName = :sidoName
			GROUP BY t.typeName
		""")
	List<TypeCountDto> getListByTypeAndSidoName(@Param("sidoName") String sidoName);
	
	// 시군구별 병원 유형 수
	@Query("""
			SELECT new edu.pnu.dto.TypeCountDto(t.typeName, COUNT(b))
			FROM BasicInfo b
			JOIN b.typeCode t
			JOIN b.sidoCode sd
			JOIN b.sigunguCode sg
			WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
			GROUP BY t.typeName
		""")
	List<TypeCountDto> getListByTypeAndSidoNameAndSigunguName(
										@Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName);
}