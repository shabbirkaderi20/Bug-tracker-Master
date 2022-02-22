package com.comments.vo;

import javax.persistence.Lob;

public class Bug {

    private Long bugId;
    private String bugNote;
    private String severity;
    private String status;
    private byte[] image;
    private String imageName;
    private Long staffId;
    private boolean isStaffAssigned;
    private boolean isBugSolved;
    private Long projectId;
}
