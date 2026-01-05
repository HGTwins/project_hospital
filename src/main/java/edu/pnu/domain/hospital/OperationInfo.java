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

    // ===== 실제 FK 컬럼 =====
    @Column(name = "hospital_id", length = 200, nullable = false)
    private Long hospitalId;

    // ===== 연관관계 (읽기 전용) =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
    		name = "hospital_id",
            insertable = false,
            updatable = false
    )
    private BasicInfo basicInfo;

    // ===== 나머지 컬럼 =====
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
}
