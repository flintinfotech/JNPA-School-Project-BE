package com.flint.sample_be_springboot.entity.websiteModuleEntities.classRoom;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "SUBJECT_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubjectEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "SUBJECT_ID")
    private Long subjectId;

    @ManyToOne(targetEntity = ClassRoomEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "CLASS_ROOM_ID")
    private ClassRoomEntity classRoomEntity;

    @Column(name = "SUBJECT_NAME")
    private String subjectName;

    @Column(name = "SUBJECT_DESCRIPTION")
    private String subjectDescription;


}
