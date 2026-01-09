package edu.pnu.domain.hospital;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "offset")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Offset {

    @Id
    @Column(name = "hospital_id")
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
    @JsonIgnore
    private BasicInfo basicInfo;
}
