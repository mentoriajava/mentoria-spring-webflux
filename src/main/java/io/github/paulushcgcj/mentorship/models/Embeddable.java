package io.github.paulushcgcj.mentorship.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Embeddable {
  private String code;
  private String description;
}