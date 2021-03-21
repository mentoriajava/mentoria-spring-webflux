package io.github.paulushcgcj.mentorship.models;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

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
public class FundingRound {
  private Long id;
  private String round;
  private URL source;
  private String description;
  private BigDecimal amount;
  private Currency currency;
  private LocalDate date;
  @Singular
  private List<InvestmentInfo> investments;
  private ReferenceInfo company;
}
