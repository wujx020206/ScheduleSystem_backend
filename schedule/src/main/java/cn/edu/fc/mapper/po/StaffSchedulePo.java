package cn.edu.fc.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_staff_schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffSchedulePo {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String staffId;
    private LocalDateTime start;
    private LocalDateTime end;
}
