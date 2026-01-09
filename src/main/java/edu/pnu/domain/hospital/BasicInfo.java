package edu.pnu.domain.hospital;

import java.util.ArrayList;
import java.util.List;

import edu.pnu.domain.board.Board;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "basic_info")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicInfo {

    @Id
    @Column(name = "hospital_id")
    private Long hospitalId;
    
    @Column(name = "care_enc_code", length = 200)
    private String careEncCode;

    @Column(name = "institution_name", length = 200, nullable = false)
    private String institutionName;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "call", length = 200, nullable = false)
    private String call;

    @Column(name = "homepage", length = 200)
    private String homepage;

    // ===== 연관관계 (조회용) =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sido_code",
        insertable = false,
        updatable = false
    )
    private SidoCode sidoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "type_code",
        insertable = false,
        updatable = false
    )
    private TypeCode typeCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
    	@JoinColumn(
    			name = "sido_code",
    		    insertable = false,
    		    updatable = false
    	),
    	@JoinColumn(
    			name = "sigungu_code",
    		    insertable = false,
    		    updatable = false
    	)
    })
    private SigunguCode sigunguCode;
    
    @OneToOne(mappedBy = "basicInfo", fetch = FetchType.LAZY)
    private Offset offset;
    
    @OneToMany(mappedBy = "basicInfo", fetch = FetchType.LAZY)
    private List<DeptDoctor> deptDoctors = new ArrayList<>();
    
    @OneToMany(mappedBy = "basicInfo", fetch = FetchType.LAZY)
    private List<Board> boardList = new ArrayList<>();
}