package edu.pnu.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MedicalInfoSearch {
	private Double swLat = 33.0;
	private Double neLat = 38.6;
	private Double swLng = 124.5;
	private Double neLng = 131.0;
	private Integer level = 1;
}
