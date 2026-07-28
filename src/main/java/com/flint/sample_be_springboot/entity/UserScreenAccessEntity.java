package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "USER_SCREEN_ACCESS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"USER_ID", "SCREEN_ID"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserScreenAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SCREEN_ACCESS_ID")
    private Long userScreenAccessId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_ID")
    private ScreenMaster screen;

}

