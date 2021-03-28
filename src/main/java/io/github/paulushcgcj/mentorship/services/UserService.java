package io.github.paulushcgcj.mentorship.services;

import io.github.paulushcgcj.mentorship.models.user.ServiceUser;
import io.github.paulushcgcj.mentorship.repositories.GenericFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@Slf4j
public class UserService {

  private final GenericFileRepository<ServiceUser> repository;

  public UserService(
    GenericFileRepository<ServiceUser> repository,
    @Value("${io.github.paulushcgcj.stub.user}") Path stubFile
  ) {
    this.repository = repository;
    this.repository.setUp(stubFile, ServiceUser.class);
  }

}
