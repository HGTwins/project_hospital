package edu.pnu.domain.board;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.pnu.domain.hospital.BasicInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(exclude = {"basicInfo", "member"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	
	@Column(name = "hospital_id")
	private Long hospitalId;
	
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
	
	@CreationTimestamp
	@Column(updatable = false)
	private Date createDate;
	
	@Column(name = "username")
	private String username;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "username", 
        insertable = false, 
        updatable = false
    )
	@JsonIgnore
	private Member member;
}
