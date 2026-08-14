package com.flint.sample_be_springboot.entity;

import com.flint.sample_be_springboot.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Table(name = "USER_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "USER_ID")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_DETAILS_ID", unique = true)
    private EmployeeDetailsEntity employeeDetails;

    @NotNull
    @Column(name = "USERNAME")
    private String userName;

    @Size(min = 8)
    @Column(name = "PASSWORD")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @NotNull
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotNull
    @Column(name = "LAST_NAME")
    private String lastName;

    @NotNull
    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @NotNull
    @Size(max = 50)
    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "SECTION")
    private String section;

    @Column(name = "MEDIUM")
    private String medium;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserScreenAccessEntity> screenAccesses;

    @Embedded
    private AuditDetails auditDetails;

}
