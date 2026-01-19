package edu.pnu.dto;

import edu.pnu.domain.hospital.BasicInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EssentialHospitalDto {
	private HospitalDto hospital;
	private String deptName;
	
	public EssentialHospitalDto(BasicInfo basicInfo, String deptName) {
        this.hospital = HospitalDto.from(basicInfo); 
        this.deptName = deptName;
    }
}
