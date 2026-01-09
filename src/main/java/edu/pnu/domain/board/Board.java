package edu.pnu.domain.board;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.pnu.domain.hospital.BasicInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(exclude = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board {
	@Id
	@GeneratedValue
	private Long seq;

    // ===== 연관관계 (읽기 전용) =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
    		name = "hospital_id",
            insertable = false,
            updatable = false
    )
    @JsonIgnore
    private BasicInfo basicInfo;
    
	@Column(nullable = false)
	private String content;
	@SuppressWarnings("deprecation")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createDate;
	@Column(updatable = false)
	private Long cnt;
	
	@ManyToOne
	@JoinColumn(name = "username", nullable = false, updatable = false)
	@JsonIgnore
	private Member member;
}
