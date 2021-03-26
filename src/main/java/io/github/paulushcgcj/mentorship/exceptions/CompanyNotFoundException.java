package io.github.paulushcgcj.mentorship.exceptions;

public class CompanyNotFoundException extends MentorshipBaseException {
  public CompanyNotFoundException(String companyId) {
    super(String.format("No Company found for id %s",companyId),404,null);
  }
}
