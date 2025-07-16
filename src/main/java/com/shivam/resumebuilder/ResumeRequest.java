package com.shivam.resumebuilder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResumeRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Invalid Email Formate")
    private String email;
    @Size(min = 10 , message = "Summary must be at least 10 characters")
    private String summary;

    public String getEmail() {
        return email;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
