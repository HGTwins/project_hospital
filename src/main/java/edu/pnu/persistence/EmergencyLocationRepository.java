package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.EmergencyLocation;

public interface EmergencyLocationRepository extends JpaRepository<EmergencyLocation, Long> {
	List<EmergencyLocation> getBySigungu(String sigungu);
}
