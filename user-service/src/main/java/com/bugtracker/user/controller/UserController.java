package com.bugtracker.user.controller;

import com.bugtracker.user.VO.ResponseTemplateVO;
import com.bugtracker.user.dto.UpdateUserDto;
import com.bugtracker.user.dto.UserDto;
import com.bugtracker.user.entity.AuthRequest;
import com.bugtracker.user.entity.User;
import com.bugtracker.user.service.UserService;
import com.bugtracker.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* New controller changes for enhancement comments */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @GetMapping(value= "/auth")
    public String userLogin(@RequestParam String userName, @RequestParam String password, @RequestParam Long roleId) {

        return userService.userLogin(userName, password, roleId);
    }

    /* Use the OAuth2 for multiple login options */
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        }
        catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }

    /* Register with only mandatory details also if partial */
    @PostMapping(value= "/registerUserAccount")
    public User registerUser(@RequestBody UserDto userDto) {

        return userService.registerUser(userDto);
    }

    /* Implement search functionality for search with name, ID, role */
    @GetMapping(value= "/getUser/{id}")
    public ResponseTemplateVO getUserById(@PathVariable("id") Long userId) {

        return userService.getUser(userId);
    }

    /* Update partial data using patch method */
    @PutMapping(value= "/updateUserAccount")
    public User updateUser(@RequestBody UpdateUserDto updateUserDto) {

        return userService.updateUser(updateUserDto);
    }

    /* Make more secure by improving de-crypt and en-crypt to unknown algorithm */
    @PutMapping(value= "/changePassword")
    public User changePassword(@RequestParam Long userId, @RequestParam String currentPassword, @RequestParam String newPassword) {

        return userService.changePassword(userId, currentPassword, newPassword);
    }

    @PutMapping(value= "/changeEmail")
    public User changeEmail(@RequestParam Long userId, @RequestParam String password, @RequestParam String email) {

        return userService.changeEmail(userId, password, email);
    }

    /* Implement search functionality for search with name, ID, role */
    @GetMapping(value= "/getStaffById/{id}")
    public User getStaffById(@PathVariable("id") Long userId) {

        return userService.viewStaffById(userId);
    }

    /* Implement search functionality for search with name, ID, role */
    @GetMapping(value= "/getClientById/{id}")
    public User getClientById(@PathVariable("id") Long userId) {

        return userService.viewClientById(userId);
    }

    /* Implement search functionality for search with name, ID, role */
    @GetMapping(value= "/getAllClient")
    public List<User> getALlClient() {

        return userService.viewAllClient();
    }

    /* Implement search functionality for search with name, ID, role */
    @GetMapping(value= "/getAllStaff")
    public List<User> getALlStaff() {

        return userService.viewAllStaff();
    }

    /* Delete by name and id both */
    @DeleteMapping("/deleteClient/{id}")
    public Boolean deleteClientById(@PathVariable("id") Long clientId) {
        return userService.deleteClientById(clientId);
    }

    /* Delete by name and id both */
    @DeleteMapping("/deleteStaff/{id}")
    public Boolean deleteStaffById(@PathVariable("id") Long staffId) {
        return userService.deleteStaffById(staffId);
    }
}
