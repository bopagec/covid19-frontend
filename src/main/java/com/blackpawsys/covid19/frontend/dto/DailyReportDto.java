package com.blackpawsys.covid19.frontend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_EMPTY)
public class DailyReportDto {

  private Long confirmed;
  private Long deaths;
  private String country;
  private String state;
  private Long newCases;
  private Long newDeaths;
  private LocalDateTime lastUpdated;
}
