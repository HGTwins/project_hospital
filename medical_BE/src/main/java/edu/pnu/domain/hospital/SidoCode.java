package edu.pnu.domain.hospital;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sido_code")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SidoCode {

    @Id
    @Column(name = "sido_code", length = 20)
    private String sidoCode;

    @Column(name = "sido_name", length = 20, nullable = false)
    private String sidoName;
}
