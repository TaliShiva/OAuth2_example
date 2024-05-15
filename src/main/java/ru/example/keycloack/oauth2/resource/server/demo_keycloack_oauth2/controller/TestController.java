package ru.example.keycloack.oauth2.resource.server.demo_keycloack_oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/login")
    public String login(){
        return "login work";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('AOS Admin')")
    public String delete(@PathVariable String id, @AuthenticationPrincipal Jwt jwt){
        System.out.println("jwt.getClaim(\"email\") = " + jwt.getClaim("email"));
        System.out.println("id delete = " + id);
        return "delete work";
    }


    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('AOS Admin', 'AOS User Developer')")
    public String add(){
        return "add work";
    }

    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('AOS Admin', 'AOS User Developer')")
    public String view(){
        return "read work";
    }
}
