package io.github.paulushcgcj.mentorship.exceptions;

import lombok.Getter;

public class MentorshipBaseException extends RuntimeException {
  @Getter
  private int statusCode;
  @Getter
  private Throwable originalError;

  public MentorshipBaseException(String message, int statusCode, Throwable originalError) {
    super(message);
    this.statusCode = statusCode;
    this.originalError = originalError;
  }

  public MentorshipBaseException(String message, Throwable cause, int statusCode, Throwable originalError) {
    super(message, cause);
    this.statusCode = statusCode;
    this.originalError = originalError;
  }

  public MentorshipBaseException(Throwable cause, int statusCode, Throwable originalError) {
    super(cause);
    this.statusCode = statusCode;
    this.originalError = originalError;
  }

  public MentorshipBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int statusCode, Throwable originalError) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.statusCode = statusCode;
    this.originalError = originalError;
  }
}
