package edu.pnu.domain.hospital;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dept_code")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeptCode {

    @Id
    @Column(name = "dept_code", length = 20, nullable = false)
    private String deptCode;

    @Column(name = "dept_name", length = 45, nullable = false)
    private String deptName;
}