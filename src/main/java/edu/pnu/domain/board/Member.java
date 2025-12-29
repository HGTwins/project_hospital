package edu.pnu.domain.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(exclude = "boardList")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
	@Id
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String alias;
	@Enumerated(EnumType.STRING)
	private Role role;
	private Boolean enabled;
}
