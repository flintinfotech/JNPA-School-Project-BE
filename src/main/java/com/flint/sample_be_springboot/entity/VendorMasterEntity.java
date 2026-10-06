package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "VENDOR_MASTER_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "VENDOR_MASTER_ID")
    private Long vendorMasterId;



}
