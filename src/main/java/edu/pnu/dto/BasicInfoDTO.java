package edu.pnu.dto;

import edu.pnu.domain.hospital.BasicInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasicInfoDTO {
	
	private Long hospitalId;
    private String careEncCode;
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

    public static BasicInfoDTO from(BasicInfo entity) {
        return BasicInfoDTO.builder()
        		.hospitalId(entity.getHospitalId())
                .careEncCode(entity.getCareEncCode())
                .institutionName(entity.getInstitutionName())
                .address(entity.getAddress())
                .call(entity.getCall())
                .homepage(entity.getHomepage())

                .sidoCode(
                    entity.getSidoCode() != null 
                        ? entity.getSidoCode().getSidoCode() 
                        : null
                )
                .sidoName(
                    entity.getSidoCode() != null 
                        ? entity.getSidoCode().getSidoName() 
                        : null
                )

                .typeCode(
                    entity.getTypeCode() != null 
                        ? entity.getTypeCode().getTypeCode() 
                        : null
                )
                .typeName(
                    entity.getTypeCode() != null 
                        ? entity.getTypeCode().getTypeName() 
                        : null
                )
                
                .sigunguCode(
                        entity.getSigunguCode() != null 
                            ? entity.getSigunguCode().getId().getSigunguCode() 
                            : null
                    )
                .sigunguName(
                        entity.getSigunguCode() != null 
                            ? entity.getSigunguCode().getSigunguName() 
                            : null
                    )
                .build();
    }
}
