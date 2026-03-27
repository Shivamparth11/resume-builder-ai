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

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    // Constructor
    public ResumeResponse() {}

    public ResumeResponse(int id, String name, String email, String summary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.summary = summary;
    }

    // Getters & Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getAiFeedback() { return aiFeedback; }

    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }
}