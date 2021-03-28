package io.github.paulushcgcj.mentorship.repositories;

import io.github.paulushcgcj.mentorship.models.user.ServiceUser;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends GenericFileRepository<ServiceUser> {
}
