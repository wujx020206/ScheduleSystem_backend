package cn.edu.fc.mapper.po;

import io.lettuce.core.StrAlgoArgs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Table(name = "staff_preference")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferencePo {
    private Byte type;

    private Long staffId;

    private String value;
}
