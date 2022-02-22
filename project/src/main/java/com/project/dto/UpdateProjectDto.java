package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectDto {

    private Long projectId;
    private String projectName;
    private String projectInfo;
    private boolean isOnGoing;
    private Date endedOn;
}
