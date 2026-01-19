package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.hospital.SidoCode;

public interface SidoRepository extends JpaRepository<SidoCode, String> {
	// 시도명 가져오기
	@Query("SELECT sd.sidoName FROM SidoCode sd")
	List<String> getListAllSidoNames();
}
