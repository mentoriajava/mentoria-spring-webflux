package io.github.paulushcgcj.mentorship.exceptions;

public class CompanyNotFoundException extends Exception {
  public CompanyNotFoundException(String companyId) {
    super(String.format("No Company found for id %s",companyId));
  }
}
