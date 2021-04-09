package io.github.paulushcgcj.mentorship.repositories;

import io.github.paulushcgcj.mentorship.models.user.ServiceUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepository extends GenericFileRepository<ServiceUser> {
}
