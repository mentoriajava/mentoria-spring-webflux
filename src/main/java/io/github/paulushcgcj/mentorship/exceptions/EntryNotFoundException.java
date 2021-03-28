package io.github.paulushcgcj.mentorship.exceptions;

public class EntryNotFoundException extends MentorshipBaseException {
  public EntryNotFoundException(String companyId) {
    super(String.format("No entry found for id %s",companyId),404,null);
  }
}
