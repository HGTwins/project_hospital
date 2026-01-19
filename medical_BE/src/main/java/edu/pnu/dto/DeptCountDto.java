package edu.pnu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeptCountDto {
	private String deptCode;
	private Long count;
}
