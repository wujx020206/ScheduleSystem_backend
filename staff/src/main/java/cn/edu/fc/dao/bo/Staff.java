package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.openfeign.ShopDao;
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
public class Staff extends SSObject implements Serializable {
    /**
     * 门店经理
     */
    public static final Byte STOREMANAGER = 0;

    /**
     * 副经理
     */
    public static final Byte ASSISTANTMANAGER = 1;

    /**
     * 小组长
     */
    public static final Byte TEAMLEADER = 2;

    /**
     * 店员（收银）
     */
    public static final Byte CASHIER = 3;

    /**
     * 店员（导购）
     */
    public static final Byte GUIDE = 4;

    /**
     * 店员（库房）
     */
    public static final Byte WAREHOUSE = 5;

    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Staff.class);

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String shopId;

    private Shop shop;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ShopDao shopDao;

    public Shop getShop() {
        if (null == this.shopId) {
            return null;
        }

        if (null == this.shop && null != this.shopDao) {
            this.shop = this.shopDao.getShopById(this.shopId).getData();
        }

        return this.shop;
    }

    @Builder
    public Staff(String id, Long creatorId, Long modifierId, String modifierName, String creatorName, LocalDateTime gmtCreate, LocalDateTime gmtModified,
                 String name, String position, String phone, String email, String shopId) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.shopId = shopId;
    }
}
