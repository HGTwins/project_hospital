package edu.pnu.dto;

import edu.pnu.domain.hospital.BasicInfo;
import edu.pnu.domain.hospital.OperationInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OperationInfoDto {
	// 1. 병원 기본 정보 (이미 만드신 DTO 활용)
    private HospitalDto hospital;

    // 2. 주차 및 휴무 정보
    private Boolean parkingFeeYn;
    private String parkingNote;
    private String closedSunday;
    private String closedHoliday;

    // 3. 점심 시간
    private String lunchWeekday;
    private String lunchSaturday;

    // 4. 요일별 마감 시간 (야간 진료 판별 등에 활용)
    private String endMonday;
    private String endTuesday;
    private String endWednesday;
    private String endThursday;
    private String endFriday;
    private String endSaturday;
    private String endSunday;

    public OperationInfoDto(BasicInfo basicInfo, OperationInfo op) {
        this.hospital = HospitalDto.from(basicInfo);

        if (op != null) {
            this.parkingFeeYn = op.getParkingFeeYn();
            this.parkingNote = op.getParkingNote();
            this.closedSunday = op.getClosedSunday();
            this.closedHoliday = op.getClosedHoliday();
            this.lunchWeekday = op.getLunchWeekday();
            this.lunchSaturday = op.getLunchSaturday();
            this.endMonday = op.getEndMonday();
            this.endTuesday = op.getEndTuesday();
            this.endWednesday = op.getEndWednesday();
            this.endThursday = op.getEndThursday();
            this.endFriday = op.getEndFriday();
            this.endSaturday = op.getEndSaturday();
            this.endSunday = op.getEndSunday();
        }
    }
}
