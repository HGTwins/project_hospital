package edu.pnu.domain.hospital;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@SuppressWarnings("serial")
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SigunguCodeId implements Serializable {
	@Column(name = "sido_code")
    private String sidoCode;
	
	@Column(name = "sigungu_code")
    private String sigunguCode;
}

