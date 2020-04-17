package com.blackpawsys.covid19.frontend.Controller;

import com.blackpawsys.covid19.frontend.component.Direction;
import com.blackpawsys.covid19.frontend.component.Response;
import com.blackpawsys.covid19.frontend.dto.DailyReportDataDto;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
@Slf4j
public class Covid19FrontEndController {

  @Value("${covid19.service.api.report.date}")
  private String dayReportUri;

  @Value("${covid19.service.api.report.all}")
  private String allReportUri;

  @Value("${covid19.service.api.report.country}")
  private String countryReportUri;

  @Value("${app.heading}")
  private String appHeading;

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");
  public static final DateTimeFormatter CURR_DATE_AS_OF = DateTimeFormatter.ofPattern("dd MMM yyyy");

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping(value = {"/report", "/report/{direction}/{date}"})
  private String getDailyReport(Model model, @PathVariable Optional<Direction> direction, @PathVariable Optional<String> date) {
    String uri = allReportUri;
    LocalDate localDate = updateDates(model, direction, date);

    if (direction.isPresent() && date.isPresent()) {
      StringBuilder sb = new StringBuilder();

      sb.append(dayReportUri)
          .append("/")
          .append(FORMATTER.format(localDate));

      uri = sb.toString();
    }

    log.info("calling getDailyReport in Covid19FrontEndController, {}", uri);

    Response<DailyReportDataDto> response = restTemplate.getForObject(URI.create(uri), Response.class);
    model.addAttribute("reportResponse", response.getPayload());
    model.addAttribute("direction", false);
    model.addAttribute("appHeading", appHeading);

    return "all_report";
  }


  private LocalDate updateDates(Model model, Optional<Direction> direction, Optional<String> date) {
    LocalDate targetDate = LocalDate.now().minusDays(1);
    try {
      if (direction.isPresent() && date.isPresent()) {
        targetDate = LocalDate.parse(date.get(), FORMATTER);

        if (direction.get().equals(Direction.previous)) {
          LocalDate prevDate = targetDate.minusDays(1);
          model.addAttribute("prevDate", FORMATTER.format(prevDate));
          model.addAttribute("nextDate", FORMATTER.format(prevDate.plusDays(1)));
          model.addAttribute("navigator", "prevDate");
        } else if (direction.get().equals(Direction.next)) {

          if (targetDate.isBefore(LocalDate.now())) {
            LocalDate nextDate = targetDate.plusDays(1);
            model.addAttribute("prevDate", FORMATTER.format(nextDate.minusDays(1)));
            model.addAttribute("nextDate", FORMATTER.format(nextDate));
            model.addAttribute("navigator", "nextDate");
          }
        }
        model.addAttribute("targetDate", FORMATTER.format(targetDate));
      } else {
        setDefaultDates(model);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      setDefaultDates(model);
    }

    return targetDate;
  }

  private void setDefaultDates(Model model){
    LocalDate targetDate = LocalDate.now().minusDays(1);

    model.addAttribute("prevDate", FORMATTER.format(targetDate.minusDays(1)));
    model.addAttribute("targetDate", CURR_DATE_AS_OF.format(targetDate));
    model.addAttribute("nextDate", FORMATTER.format(targetDate.plusDays(1)));
    model.addAttribute("navigator", "yesterday");
  }

  @GetMapping(value = {"/report/country/{country}"})
  private String getCountryReport(@PathVariable String country, Model model) {
    String uri = null;
    try {
      uri = countryReportUri.concat("/").concat(URLEncoder.encode(country, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage());
    }

    log.info("calling getDailyReport in Covid19FrontEndController, {}", uri);

    Response<DailyReportDataDto> response = restTemplate.getForObject(URI.create(uri), Response.class);
    model.addAttribute("reportResponse", response.getPayload());
    model.addAttribute("country", country.toUpperCase());
    model.addAttribute("appHeading", appHeading);

    setDefaultDates(model);

    return "country_report";
  }

}
