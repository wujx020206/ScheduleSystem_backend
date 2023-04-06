package cn.edu.fc.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AllRulesDto {
    private Integer weekDayOpenRule;
    private Integer weekDayCloseRule;
    private Integer weekendOpenRule;
    private Integer weekendCloseRule;
    private Integer maxHourPerWeek;
    private Integer maxHourPerDay;
    private Integer leastHourPerPeriod;
    private Integer maxHourPerPeriod;
    private Integer maxWorkWeek;
    private Integer lunchBegin;
    private Integer lunchEnd;
    private Integer dinnerBegin;
    private Integer dinnerEnd;
    private LocalTime breakTime;
    private Integer prepareTime;
    private Integer preparePeople;
    private List<String> prepareStation;
    private Float workPeople;
    private List<String> workStation;
    private Integer leastPeople;
    private Integer endHour;
    private Integer endPeople;
    private List<String> endStation;

    public void setEndPeople(Long x, Long y) {
        // Todo
        throw new UnsupportedOperationException();
    }
}
