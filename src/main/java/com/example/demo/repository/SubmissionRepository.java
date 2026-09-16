package com.example.demo.repository;

import com.example.demo.entity.SubmissionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<SubmissionEntity, UUID> {}
