package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.SigunguCode;
import edu.pnu.domain.hospital.SigunguCodeId;

public interface SigunguRepository extends JpaRepository<SigunguCode, SigunguCodeId> {
	// 시군구명 가져오기
	@Query("""
			SELECT sg.sigunguName
			FROM SigunguCode sg
			JOIN sg.sidoCode sd
			WHERE sd.sidoName = :sidoName
			""")
	List<String> getListSigunguNamesBySidoName(@Param("sidoName") String sidoName);
}
