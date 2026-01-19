package edu.pnu.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MedicalChartSearch {
	private String sidoName;
	private String sigunguName;
	private Long topN = 4L;
}
