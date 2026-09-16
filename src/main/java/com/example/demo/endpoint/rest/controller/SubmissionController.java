package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.Submission;
import com.example.demo.mapper.SubmissionMapper;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.service.SubmissionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class SubmissionController {

  private final SubmissionService service;
  private final SubmissionMapper mapper;
  private final SubmissionRepository repository;

  @PostMapping(value = "/submissions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Submission createSubmission(
      @RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
    var entity = service.create(file, email);
    return mapper.toRest(entity);
  }

  @GetMapping("/submissions")
  public List<Submission> listSubmissions() {
    return repository.findAll().stream().map(mapper::toRest).toList();
  }
}
