package edu.pnu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardMemberSearch {
	private String username;
	private Integer page = 0;
	private Integer size = 10;
}
