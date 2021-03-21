package io.github.paulushcgcj.mentorship.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {
  private String id;
  private String name;
  private String description;
  private String overview;
  private String email;
  private String phone;
  private String permalink;
  private String category;
  private Long employees;
  private LocalDate foundation;
  private IPO ipo;

  @Singular
  private Map<String, URL> presences;

  @Singular
  private List<String> tags;

  @Singular
  private List<String> aliases;

  private ZonedDateTime created;
  private ZonedDateTime updated;

  @Singular
  private List<VisualAsset> assets;

  @Singular
  private List<ReferenceInfo> products;

  @Singular
  private List<Relationship> relationships;

  @Singular
  private List<Competitor> competitions;

  @Singular
  private List<Providership> providerships;

  private BigDecimal raisedMoney;

  @Singular
  private List<FundingRound> fundingRounds;

  @Singular
  private List<Investment> investments;

  private Acquisition acquisitionInfo;

  @Singular
  private List<Acquisition> acquisitions;

  @Singular
  private List<Address> offices;

  @Singular
  private List<Milestone> milestones;

  @Singular
  private List<Embeddable> videoEmbeds;

  @Singular
  private List<VisualAsset> screenshots;

  @Singular
  private List<ExternalReference> externalLinks;

  @Singular
  private List<Partner> partners;

}
