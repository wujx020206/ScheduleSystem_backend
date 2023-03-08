package cn.edu.fc.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "forecast_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataPo {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String storeId;

    private LocalDate date;

    private String beginTime;

    private String endTime;

    private Float num;
}
