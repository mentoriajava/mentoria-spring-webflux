package io.github.paulushcgcj.mentorship.models;

import java.net.URL;
import java.time.LocalDate;

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
public class Milestone {
  private Long id;
  private String description;
  private LocalDate date;
  private URL source;
  private String sourceText;
  private String sourceDescription;
  private String type;
  private String stoned;
  private String stonedType;
  private ReferenceInfo acquirer;
  private ReferenceInfo stoneable;
}
