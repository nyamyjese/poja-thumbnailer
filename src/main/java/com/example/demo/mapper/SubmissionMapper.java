package com.example.demo.mapper;

import com.example.demo.dto.Submission;
import com.example.demo.entity.SubmissionEntity;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

  public Submission toRest(SubmissionEntity entity) {
    return Submission.builder()
        .id(entity.getId())
        .email(entity.getEmail())
        .thumbnailKey(entity.getThumbnailKey())
        .createdAt(entity.getCreatedAt())
        .build();
  }
}
