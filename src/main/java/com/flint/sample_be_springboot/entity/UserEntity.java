package com.flint.sample_be_springboot.entity;

import com.flint.sample_be_springboot.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    @NotNull
    @Column(name = "USERNAME")
    private String userName;

    @NotNull
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

    @Embedded
    private AuditDetails auditDetails;

}
