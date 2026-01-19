package edu.pnu.domain.hospital;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operation_info")
public class OperationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    private Long operationId;

    // ===== 연관관계 (읽기 전용) =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
    		name = "hospital_id",
            insertable = false,
            updatable = false
    )
    private BasicInfo basicInfo;

    // ===== 나머지 컬럼 =====
    @Column(name = "location_place")
    private String locationPlace;
    
    @Column(name = "location_direction")
    private String locationDirection;
    
    @Column(name = "location_distance")
    private String locationDistance;
    
    @Column(name = "parking_fee_yn")
    private Boolean parkingFeeYn;

    @Column(name = "parking_note", length = 200)
    private String parkingNote;

    @Column(name = "closed_sunday", length = 100)
    private String closedSunday;

    @Column(name = "closed_holiday", length = 100)
    private String closedHoliday;

    @Column(name = "lunch_weekday", length = 50)
    private String lunchWeekday;

    @Column(name = "lunch_saturday", length = 50)
    private String lunchSaturday;

    @Column(name = "start_monday", length = 8)
    private String startMonday;

    @Column(name = "end_monday", length = 8)
    private String endMonday;
    
    @Column(name = "start_tuesday", length = 8)
    private String startTuesday;

    @Column(name = "end_tuesday", length = 8)
    private String endTuesday;
    
    @Column(name = "start_wednesday", length = 8)
    private String startWednesday;

    @Column(name = "end_wednesday", length = 8)
    private String endWednesday;
    
    @Column(name = "start_thursday", length = 8)
    private String startThursday;

    @Column(name = "end_thursday", length = 8)
    private String endThursday;
    
    @Column(name = "start_friday", length = 8)
    private String startFriday;

    @Column(name = "end_friday", length = 8)
    private String endFriday;
    
    @Column(name = "start_saturday", length = 8)
    private String startSaturday;

    @Column(name = "end_saturday", length = 8)
    private String endSaturday;
    
    @Column(name = "start_sunday", length = 8)
    private String startSunday;

    @Column(name = "end_sunday", length = 8)
    private String endSunday;
}
