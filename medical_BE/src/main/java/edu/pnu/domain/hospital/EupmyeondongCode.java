package edu.pnu.domain.hospital;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "eupmyeondong_code")
@Entity
public class EupmyeondongCode {

    @Id
    @Column(name = "eupmyeondong_code", length = 20, nullable = false)
    private String eupmyeondongCode;

    @Column(name = "eupmyeondong_name", length = 45, nullable = false)
    private String eupmyeondongName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sido_code",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_eup_sido_code")
    )
    private SidoCode sidoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "sido_code", referencedColumnName = "sido_code", insertable = false, updatable = false),
        @JoinColumn(name = "sigungu_code", referencedColumnName = "sigungu_code", insertable = false, updatable = false)
    })
    private SigunguCode sigunguCode;
}