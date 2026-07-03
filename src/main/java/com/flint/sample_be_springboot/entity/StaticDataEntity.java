package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "STATIC_DATA_ENTITY")
@Entity
@Getter
@Setter
public class StaticDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DD_ID")
    private Long id;

    @Column(name = "DD_KEY")
    private String dropDrownKey;

    @Column(name = "DD_VALUE")
    private String dropDrownValue;

}
