package com.bug.dto;

import com.bug.entity.Bug;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugWithImage {

    private BugWithoutImage bugWithoutImage;
    private ResponseEntity<byte[]> file;

}
