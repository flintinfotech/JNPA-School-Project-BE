package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "CLASS_ROOM_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "CLASS_ROOM_ID")
    private Long classRoomId;

    @Column(name = "CLASS_ROOM_NAME")
    private String classRoomName;

    @Column(name = "ACADEMIC_YEAR_NAME")
    private String academicYearName;

    @Column(name = "DESCRIPTION")
    private String description;

}
