package com.example.demo.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Submission {
  private UUID id;
  private String email;
  private String thumbnailKey;
  private Instant createdAt;
}
