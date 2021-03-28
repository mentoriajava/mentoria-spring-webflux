package io.github.paulushcgcj.mentorship.models.company;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.paulushcgcj.mentorship.models.IdentifiableEntry;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company implements IdentifiableEntry<Company> {
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
  private BigDecimal raisedMoney;

  private ZonedDateTime created;
  private ZonedDateTime updated;
  private IPO ipo;

  @Singular
  private Map<String, URL> presences;

  @Singular
  private List<String> tags;

  @Singular
  private List<String> aliases;

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

  public static List<String> comparingFields() {
    return Arrays.asList(
      "name",
      "description",
      "overview",
      "email",
      "phone",
      "permalink",
      "category",
      "employees",
      "foundation",
      "raisedMoney"
    );
  }
}
