package edu.pnu.persistence.hospital;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.BasicInfo;
import edu.pnu.domain.hospital.Offset;

public interface OffsetRepository extends JpaRepository<Offset, String> {
	@Query(value = """
            SELECT b.* FROM basic_info b
            JOIN offset o ON b.care_enc_code = o.care_enc_code
            WHERE ST_Distance_Sphere(
                point(o.longitude, o.latitude), 
                point(:longitude, :latitude)
            ) <= :distance * 1000
            """, nativeQuery = true)
    List<BasicInfo> findByLocation(@Param("longitude") double longitude, @Param("latitude") double latitude,
    		@Param("distance") int distance);
}
