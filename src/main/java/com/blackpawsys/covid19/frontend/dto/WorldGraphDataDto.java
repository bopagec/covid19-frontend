package com.blackpawsys.covid19.frontend.dto;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class WorldGraphDataDto {
  private LocalDateTime lastUpdated;
  private Long newCases;
  private Long newDeaths;
}
