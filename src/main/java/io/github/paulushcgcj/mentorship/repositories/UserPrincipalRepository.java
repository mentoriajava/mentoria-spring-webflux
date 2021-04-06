package io.github.paulushcgcj.mentorship.repositories;

import io.github.paulushcgcj.mentorship.models.user.UserPrincipalInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserPrincipalRepository extends PagingAndSortingRepository<UserPrincipalInfo, UUID> {
  UserPrincipalInfo findByUsername(String username);
}
