package edu.pnu.dto;

import edu.pnu.domain.hospital.BasicInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDto {
	
	private Long hospitalId;
    private String institutionName;
    private String address;
    private String call;
    private String homepage;

    private String sidoCode;
    private String sidoName;

    private String typeCode;
    private String typeName;
    
    private String sigunguCode;
    private String sigunguName;
    
    private double latitude;
    private double longitude;
 
    
    // JPQL에서 직접 호출할 생성자
    public HospitalDto(BasicInfo entity) {
        this.hospitalId = entity.getHospitalId();
        this.institutionName = entity.getInstitutionName();
        this.address = entity.getAddress();
        this.call = entity.getCall();
        this.homepage = entity.getHomepage();

        // Sido
        if (entity.getSidoCode() != null) {
        	this.sidoCode = entity.getSidoCode().getSidoCode();
        	this.sidoName = entity.getSidoCode().getSidoName();
        }

        // Type
        if (entity.getSidoCode() != null) {
        	this.typeCode = entity.getTypeCode().getTypeCode();
        	this.typeName = entity.getTypeCode().getTypeName();
        }

        // Sigungu
        if (entity.getSigunguCode() != null) {
            this.sigunguCode = entity.getSigunguCode().getId().getSigunguCode();
            this.sigunguName = entity.getSigunguCode().getSigunguName();
        }

        // Offset (Null 방어 코드 권장)
        if (entity.getOffset() != null) {
            this.latitude = entity.getOffset().getLatitude();
            this.longitude = entity.getOffset().getLongitude();
        }
    }

    public static HospitalDto from(BasicInfo entity) {
        return new HospitalDto(entity);
    }
}
