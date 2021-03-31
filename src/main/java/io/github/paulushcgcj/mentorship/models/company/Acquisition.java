package io.github.paulushcgcj.mentorship.models.company;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Currency;

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
public class Acquisition {
  private BigDecimal amount;
  private Currency currency;
  private String termCode;
  private URL source;
  private String sourceDescription;
  private LocalDate date;
  private ReferenceInfo acquiringCompany;
}
