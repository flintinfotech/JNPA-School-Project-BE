package com.flint.sample_be_springboot.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "SCREEN_MASTER")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScreenMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCREEN_ID")
    private Long screenId;

    @NotNull
    @Column(name = "SCREEN_NAME")
    private String screenName;

    @OneToMany(mappedBy = "screen")
    private List<UserScreenAccessEntity> userScreenAccesses;

}
