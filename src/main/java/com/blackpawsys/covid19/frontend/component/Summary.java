package com.blackpawsys.covid19.frontend.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class Summary {

  private Long totalConfirmed;
  private Long totalDeaths;
  private Long totalNewCases;
  private Long totalNewDeaths;
}
