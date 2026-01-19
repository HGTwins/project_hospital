package edu.pnu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypeCountDto {
	private String typeName;
	private Long count;
}
