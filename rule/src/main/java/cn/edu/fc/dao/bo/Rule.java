package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.openfeign.StoreDao;
import cn.edu.fc.javaee.core.model.bo.SSObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule extends SSObject implements Serializable {
    /**
     * 开店规则
     */
    public static final Byte OPENRULE = 0;

    /**
     * 关店规则
     */
    public static final Byte CLOSERULE = 0;

    /**
     * 客流规则
     */
    public static final Byte PEOPLERULE = 0;

    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Rule.class);

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String value;

    @Getter
    @Setter
    private String storeId;

    private Store store;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private StoreDao storeDao;

    public Store getStore() {
        if (null == this.storeId) {
            return null;
        }

        if (null == this.store && null != this.storeDao) {
            this.store = this.storeDao.getStoreById(this.storeId).getData();
        }

        return this.store;
    }

    @Builder
    public Rule(String id, Long creatorId, Long modifierId, String modifierName, String creatorName, LocalDateTime gmtCreate, LocalDateTime gmtModified,
                String type, String value, String storeId) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.type = type;
        this.value = value;
        this.storeId = storeId;
    }
}
