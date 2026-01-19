package edu.pnu.domain.hospital;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "type_code")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TypeCode {

    @Id
    @Column(name = "type_code", length = 20)
    private String typeCode;

    @Column(name = "type_name", length = 20)
    private String typeName;
}

