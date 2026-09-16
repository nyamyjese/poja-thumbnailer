package com.example.demo.service;

import static java.io.File.createTempFile;
import static java.util.Set.of;

import com.example.demo.dto.ThumbnailRequested;
import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.entity.SubmissionEntity;
import com.example.demo.exception.BadRequestException;
import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.repository.SubmissionRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class SubmissionService {

  private static final Set<String> ALLOWED_CONTENT_TYPES = of("image/png", "image/jpeg");
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  private final SubmissionRepository repository;
  private final BucketComponent bucketComponent;
  private final EventProducer<ThumbnailRequested> eventProducer;

  @SneakyThrows
  public SubmissionEntity create(MultipartFile file, String email) {
    validate(file, email);

    var id = UUID.randomUUID();
    var originalKey = "originals/" + id + extensionOf(file);
    bucketComponent.upload(toTempFile(file), originalKey);

    var entity =
        SubmissionEntity.builder()
            .id(id)
            .email(email)
            .originalKey(originalKey)
            .thumbnailKey(null)
            .createdAt(Instant.now())
            .build();
    repository.save(entity);

    eventProducer.accept(
        List.of(
            ThumbnailRequested.builder()
                .submissionId(id)
                .originalKey(originalKey)
                .email(email)
                .build()));

    return entity;
  }

  private void validate(MultipartFile file, String email) {
    if (file == null || file.isEmpty()) {
      throw new BadRequestException("File is required");
    }
    var contentType = file.getContentType();
    if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
      throw new BadRequestException("File must be a PNG or JPEG image");
    }
    if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
      throw new BadRequestException("Email is invalid");
    }
  }

  private String extensionOf(MultipartFile file) {
    return "image/png".equals(file.getContentType()) ? ".png" : ".jpg";
  }

  @SneakyThrows
  private File toTempFile(MultipartFile file) {
    var tempFile = createTempFile("original-", extensionOf(file));
    try (var out = new FileOutputStream(tempFile)) {
      out.write(file.getBytes());
    }
    return tempFile;
  }
}
