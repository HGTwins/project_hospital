package edu.pnu.domain.hospital;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "offset")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Offset {

    @Id
    @Column(name = "hospital_id", length = 200)
    private Long hospitalId;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(
    		name = "hospital_id",
            insertable = false,
            updatable = false
    )
    private BasicInfo basicInfo;
}
