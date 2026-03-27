package com.shivam.resumebuilder;

import com.shivam.resumebuilder.repository.ResumeRepository;
import com.shivam.resumebuilder.service.AIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")  
public class ResumeController {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private AIService aiService;

    

@PostMapping("/resume")
public ResponseEntity<Map<String, Object>> addResume( @RequestBody ResumeRequest request) {

    Map<String, Object> feedback = aiService.getAIFeedback(request.getSummary());

    ResumeResponse resume = new ResumeResponse();
    resume.setName(request.getName());
    resume.setEmail(request.getEmail());
    resume.setSummary(request.getSummary());
    try {
        ObjectMapper mapper = new ObjectMapper();
        resume.setAiFeedback(mapper.writeValueAsString(feedback));
    } catch (Exception e) {
        resume.setAiFeedback("Error converting AI response");
    }

    ResumeResponse saved = resumeRepository.save(resume);

    return ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(saved));
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

    if (optionalResume.isPresent()) {

        ResumeResponse resume = optionalResume.get();

        resume.setName(request.getName());
        resume.setEmail(request.getEmail());
        resume.setSummary(request.getSummary());

        Map<String, Object> feedback = aiService.getAIFeedback(request.getSummary());

        try {
            ObjectMapper mapper = new ObjectMapper();
            resume.setAiFeedback(mapper.writeValueAsString(feedback));
        } catch (Exception e) {
            resume.setAiFeedback("Error converting AI response");
        }

        ResumeResponse updated = resumeRepository.save(resume);
        return ResponseEntity.ok(buildResponse(updated));
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Resume with ID " + id + " Not Found");
}



    @GetMapping("/resumes")
public List<Map<String, Object>> getAllResumes() {

    List<ResumeResponse> list = resumeRepository.findAll();
    List<Map<String, Object>> result = new ArrayList<>();

    for (ResumeResponse r : list) {
        result.add(buildResponse(r));
    }

    return result;
}

    @GetMapping("/resumes/{id}")
    public ResponseEntity<?> getResumeById(@PathVariable long id) {
        Optional<ResumeResponse> resume = resumeRepository.findById(id);
        if (resume.isPresent()) {
            return ResponseEntity.ok(buildResponse(resume.get()));
        } else {
            System.out.println("Resume not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume not found");
        }
    }
    private Map<String, Object> buildResponse(ResumeResponse resume) {

    Map<String, Object> response = new HashMap<>();

    response.put("id", resume.getId());
    response.put("name", resume.getName());
    response.put("email", resume.getEmail());
    response.put("summary", resume.getSummary());

    try {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parsed = mapper.readValue(resume.getAiFeedback(), Map.class);
        response.put("aiFeedback", parsed);
    } catch (Exception e) {
        response.put("aiFeedback", "Error parsing AI feedback");
    }

    return response;
}

}

