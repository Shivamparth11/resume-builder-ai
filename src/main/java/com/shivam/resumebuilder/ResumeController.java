package com.shivam.resumebuilder;

import com.shivam.resumebuilder.repository.ResumeRepository;
import com.shivam.resumebuilder.service.AIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")  // 👈 This line is important
public class ResumeController {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private AIService aiService;

    private final List<ResumeResponse> resumes = new ArrayList<>();


    @PostMapping("/resume")
    public ResponseEntity<ResumeResponse> addResume(@Valid @RequestBody ResumeRequest request) {
        String feedback = aiService.getAIFeedback(request.getSummary());
        ResumeResponse resume = new ResumeResponse();
        resume.setName(request.getName());
        resume.setEmail(resume.getEmail());
        resume.setSummary(request.getSummary());
        resume.setAiFeedback(feedback);

        ResumeResponse saved = resumeRepository.save(resume);
        return ResponseEntity.ok(saved);
        
    }
    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable Long id) {
        if (resumeRepository.existsById(id)) {
            resumeRepository.deleteById(id);
            return ResponseEntity.ok("Deleted resume with ID"+ id); // 204
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Resume with ID " + id + " not found");
        }
    }
    @PutMapping("/resumes/{id}")
    public ResponseEntity<?> updateResume(@Valid @PathVariable long id , @RequestBody ResumeRequest request){
       Optional<ResumeResponse> optionalResume = resumeRepository.findById(id);
       if (optionalResume.isPresent()){
           ResumeResponse resume = optionalResume.get();
           resume.setName(request.getName());
           resume.setEmail(request.getEmail());
           resume.setSummary(request.getSummary());
         
           String feedback = aiService.getAIFeedback(resume.getSummary());
           resume.setAiFeedback(feedback);
           
           ResumeResponse updated =  resumeRepository.save(resume);
           return ResponseEntity.ok(updated);
       }
       else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body("Resume with ID"+ id +"Not Found");
       }
    }




    @GetMapping("/resumes")
    public List<ResumeResponse> getAllResumes() {

        return resumeRepository.findAll();
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<?> getResumeById(@PathVariable long id) {
        Optional<ResumeResponse> resume = resumeRepository.findById(id);
        if (resume.isPresent()) {
            return ResponseEntity.ok(resume.get());
        } else {
            System.out.println("Resume not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume not found");
        }
    }

}

