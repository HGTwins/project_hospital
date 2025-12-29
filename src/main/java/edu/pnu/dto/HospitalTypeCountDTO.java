package edu.pnu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HospitalTypeCountDTO {
	private String typeCode;
	private Long count;
}
