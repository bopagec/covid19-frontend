package com.blackpawsys.covid19.frontend.dto;

import com.blackpawsys.covid19.frontend.component.Summary;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyReportDataDto {

  private List<DailyReportDto> dailyReportDtoList;
  private Summary summary;
  private List<WorldGraphDataDto> worldGraphData;
}
