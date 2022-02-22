package com.bugtracker.api.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    @GetMapping("/userServiceFallBack")
    public String userServiceFallBackMethod() {
        return "User Service is taking longer than Expected. " +
                " Please try again later.";
    }

    @GetMapping("/roleServiceFallBack")
    public String roleServiceFallBackMethod() {
        return "Role Service is taking longer than Expected. " +
                " Please try again later.";
    }

    @GetMapping("/projectServiceFallBack")
    public String projectServiceFallBackMethod() {
        return "Project Service is taking longer than Expected. " +
                " Please try again later.";
    }

    @GetMapping("/bugServiceFallBack")
    public String bugServiceFallBackMethod() {
        return "Bug Service is taking longer than Expected. " +
                " Please try again later.";
    }

    @GetMapping("/commentsServiceFallBack")
    public String commentsServiceFallBackMethod() {
        return "Comment Service is taking longer than Expected. " +
                " Please try again later.";
    }
}

