package cn.edu.fc.dao.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@lombok.Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Data {
    private String storeId;
    private LocalDate date;
    private String beginTime;
    private String endTime;
    private Float num;
}
