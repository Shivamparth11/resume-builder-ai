package com.shivam.resumebuilder;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String home(){
        return "Resume Builder is up and running!";
    }
}
