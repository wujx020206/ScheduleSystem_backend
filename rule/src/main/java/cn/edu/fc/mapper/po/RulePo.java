package cn.edu.fc.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rule_rule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RulePo {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String type;

    private String storeId;

    private String value;
}
