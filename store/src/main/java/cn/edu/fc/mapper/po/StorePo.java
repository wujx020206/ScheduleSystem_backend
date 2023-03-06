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
@Table(name = "store_store")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorePo {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String name;

    private String address;

    private Float size;
}
