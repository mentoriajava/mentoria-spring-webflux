package io.github.paulushcgcj.mentorship.models.user;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
class ImagesInformation {
  private URI large;
  private URI medium;
  private URI thumbnail;
}
