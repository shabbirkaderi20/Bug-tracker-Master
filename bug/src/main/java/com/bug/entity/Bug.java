package com.bug.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bug {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long bugId;
    private String bugNote;
    private String severity;
    private String status;
    @Lob
    private byte[] image;
    private String imageName;
    private Long staffId;
    private boolean isStaffAssigned;
    private boolean isBugSolved;
    private Long projectId;

}
