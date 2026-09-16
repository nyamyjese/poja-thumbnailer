package com.example.demo.service;

import com.example.demo.dto.ThumbnailRequested;
import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.util.ImageResizer;
import jakarta.mail.internet.InternetAddress;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ThumbnailRequestedService implements Consumer<ThumbnailRequested> {

  private final BucketComponent bucketComponent;
  private final SubmissionRepository repository;
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(ThumbnailRequested event) {
    var originalFile = bucketComponent.download(event.getOriginalKey());
    var thumbnailFile = ImageResizer.resizeTo256x256(originalFile);
    var thumbnailKey = "thumbnails/" + event.getSubmissionId() + ".png";
    bucketComponent.upload(thumbnailFile, thumbnailKey);

    var submission =
        repository
            .findById(event.getSubmissionId())
            .orElseThrow(
                () ->
                    new IllegalStateException("submission not found: " + event.getSubmissionId()));
    submission.setThumbnailKey(thumbnailKey);
    repository.save(submission);

    var downloadUri = bucketComponent.presign(thumbnailKey, Duration.ofDays(7));
    mailer.accept(
        new Email(
            new InternetAddress(event.getEmail()),
            List.of(),
            List.of(),
            "Your thumbnail is ready",
            "Your image has been processed. Download it here: " + downloadUri,
            List.of()));
  }
}
