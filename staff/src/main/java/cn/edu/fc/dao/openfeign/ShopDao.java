package cn.edu.fc.dao.openfeign;

import cn.edu.fc.dao.bo.Shop;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("shop-service")
public interface ShopDao {
    @GetMapping("/shops/{id}")
    InternalReturnObject<Shop> getShopById(@PathVariable String id);
}
