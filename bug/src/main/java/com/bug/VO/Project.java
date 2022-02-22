package com.bug.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    private Long projectId;
    private String projectName;
    private Date startedAt;
    private String projectInfo;
    private boolean isOnGoing;
    private Date endedOn;
    private Long clientId;
}
