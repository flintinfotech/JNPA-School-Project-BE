package com.flint.sample_be_springboot.entity.classRoom;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;
import java.util.List;

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

    @Column(name = "MEDIUM")
    private String medium;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classRoomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectEntity> subjects;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classRoomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcademicYearEntity> academicYearEntities;

    @Lob
    @Column(name = "BROCHURE")
    private byte[] brochure;

    @Embedded
    private AuditDetails auditDetails;

    public String getBrochure() {
        if (brochure != null) {
            return Base64.getEncoder().encodeToString(brochure);
        }
        return null;
    }

}
