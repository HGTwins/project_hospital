package edu.pnu.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MedicalPageSearch {
	private String sidoName;
	private String sigunguName;
	private Integer page = 0;
	private Integer size = 10;
}
