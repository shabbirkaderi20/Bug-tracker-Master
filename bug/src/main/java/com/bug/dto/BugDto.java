package com.bug.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugDto {

    private String bugNote;
    private String severity;
    private String imageName;
    private Long projectId;
}
