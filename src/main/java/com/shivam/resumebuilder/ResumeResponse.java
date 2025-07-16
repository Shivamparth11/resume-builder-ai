package com.shivam.resumebuilder;

import jakarta.persistence.*;

@Entity
public class ResumeResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String summary;

    @Column(length = 1000)
    private String aiFeedback;
    public String getAiFeedback() {
        return aiFeedback;
    }

    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }

    // No-args constructor (needed by Spring/Jackson)
    public ResumeResponse() {
    }

    // All-args constructor
    public ResumeResponse(int id, String name, String email, String summary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.summary = summary;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSummary() {
        return summary;
    }

    // Setters (optional — include if you expect to modify fields after creation)
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
