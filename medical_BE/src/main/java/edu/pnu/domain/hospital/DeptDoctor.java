package edu.pnu.domain.hospital;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dept_doctor")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeptDoctor {
	
	@EmbeddedId
    private DeptDoctorId id;

    @Column(name = "dept_doctor")
    private Integer deptDoctor;
    
    @MapsId("hospitalId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
    		name = "hospital_id",
            insertable = false,
            updatable = false
    )
    private BasicInfo basicInfo;
    
    @MapsId("deptCode")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "dept_code",
        insertable = false,
        updatable = false
    )
    private DeptCode deptCode;
    
    @Builder
    public DeptDoctor(BasicInfo basicInfo, DeptCode deptCode, Integer doctorCount) {
        this.id = new DeptDoctorId(basicInfo.getHospitalId(), deptCode.getDeptCode());
        this.basicInfo = basicInfo;
        this.deptCode = deptCode;
        this.deptDoctor = doctorCount;
    }
}
