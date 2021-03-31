package io.github.paulushcgcj.mentorship.models;

public interface IdentifiableEntry<T> {
  T withId(String id);

  String getId();
}
