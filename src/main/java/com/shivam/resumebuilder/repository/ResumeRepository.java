package com.shivam.resumebuilder.repository;

import com.shivam.resumebuilder.ResumeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<ResumeResponse , Long> {
}
