package edu.pnu.domain.hospital;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sigungu_code")
public class SigunguCode {
	@EmbeddedId
    private SigunguCodeId id;
	
	@MapsId("sidoCode") 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
    		name = "sido_code",
            insertable = false,
            updatable = false
    )
    private SidoCode sidoCode;

    @Column(name = "sigungu_name", length = 20, nullable = false)
    private String sigunguName;
    
    @Builder
    public SigunguCode(SidoCode sidoCode, String sigunguCode, String sigunguName) {
        this.id = new SigunguCodeId(sidoCode.getSidoCode(), sigunguCode);
        this.sidoCode = sidoCode;
        this.sigunguName = sigunguName;
    }
}

