package io.github.paulushcgcj.mentorship.repositories;

import org.springframework.stereotype.Repository;

import io.github.paulushcgcj.mentorship.models.user.ServiceUser;

@Repository
public class UserRepository extends GenericFileRepository<ServiceUser> {
}
