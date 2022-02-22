package com.bugtracker.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import javax.persistence.Column;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Long contactNo;
    private String email;
    private Date birthDate;
    private String address;
    private Long roleId;
}
