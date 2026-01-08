package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.DeptDoctor;
import edu.pnu.domain.hospital.DeptDoctorId;
import edu.pnu.dto.DeptCountDto;
import edu.pnu.dto.EssentialHospitalDto;

public interface DeptRepository extends JpaRepository<DeptDoctor, DeptDoctorId> {
	// 전체 진료 과목 수
	@Query("""
			SELECT new edu.pnu.dto.DeptCountDto(dc.deptName, COUNT(dd))
			FROM DeptDoctor dd
			JOIN dd.deptCode dc
			GROUP BY dc.deptName
			""")
	List<DeptCountDto> getListByDept();
	
	// 시도별 진료 과목 수
	@Query("""
			SELECT new edu.pnu.dto.DeptCountDto(dc.deptName, COUNT(dd))
			FROM DeptDoctor dd
			JOIN dd.deptCode dc
			JOIN dd.basicInfo b
			JOIN b.sidoCode sd
			WHERE sd.sidoName = :sidoName
			GROUP BY dc.deptName
			""")
	List<DeptCountDto> getListByDeptAndSidoName(@Param("sidoName") String sidoName);
	
	// 시군구별 진료 과목 수
	@Query("""
			SELECT new edu.pnu.dto.DeptCountDto(dc.deptName, COUNT(dd))
			FROM DeptDoctor dd
			JOIN dd.deptCode dc
			JOIN dd.basicInfo b
			JOIN b.sidoCode sd
			JOIN b.sigunguCode sg
			WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
			GROUP BY dc.deptName
			""")
	List<DeptCountDto> getListByDeptAndSidoNameAndSigunguName(
							@Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName);

	// 전체 필수 의료 진료 과목
	@Query(value = 
			"""
			SELECT DISTINCT new edu.pnu.dto.EssentialHospitalDto(b, dc.deptName)
			FROM DeptDoctor dd
			JOIN dd.basicInfo b
			JOIN dd.deptCode dc
			WHERE dc.deptCode IN ('10', '11', '24')
			""",
			countQuery = 
			"""
		    SELECT COUNT(DISTINCT dd.basicInfo) 
		    FROM DeptDoctor dd 
		    WHERE dd.deptCode.deptCode IN ('10', '11', '24')
		    """)
	Page<EssentialHospitalDto> getPageByAllEssential(Pageable pageable);
	
	// 시도별 필수 의료 진료 과목
	@Query(value = 
			"""
			SELECT DISTINCT new edu.pnu.dto.EssentialHospitalDto(b, dc.deptName)
			FROM DeptDoctor dd
			JOIN dd.deptCode dc
			JOIN dd.basicInfo b
			JOIN b.sidoCode sd
			WHERE sd.sidoName = :sidoName AND dc.deptCode IN ('10', '11', '24')
			""",
			countQuery = 
			"""
		    SELECT COUNT(DISTINCT dd.basicInfo) 
		    FROM DeptDoctor dd 
			JOIN dd.basicInfo b
			WHERE b.sidoCode.sidoName = :sidoName 
			AND dd.deptCode.deptCode IN ('10', '11', '24')
		    """)
	Page<EssentialHospitalDto> getPageByEssentialAndSidoName(@Param("sidoName") String sidoName, Pageable pageable);
	
	// 시군구별 필수 의료 진료 과목
	@Query(value =
			"""
			SELECT DISTINCT new edu.pnu.dto.EssentialHospitalDto(b, dc.deptName)
			FROM DeptDoctor dd
			JOIN dd.deptCode dc
			JOIN dd.basicInfo b
			JOIN b.sidoCode sd
			JOIN b.sigunguCode sg
			WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
			AND dc.deptCode IN ('10', '11', '24')
			""",
			countQuery = 
			"""
		    SELECT COUNT(DISTINCT dd.basicInfo) 
		    FROM DeptDoctor dd 
			JOIN dd.basicInfo b
			WHERE b.sidoCode.sidoName = :sidoName 
			AND b.sigunguCode.sigunguName = :sigunguName
			AND dd.deptCode.deptCode IN ('10', '11', '24')
		    """)
	Page<EssentialHospitalDto> getPageByEssentialAndSidoNameAndSigunguName(
					@Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName, Pageable pageable);
}
