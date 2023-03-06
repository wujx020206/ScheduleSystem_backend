package cn.edu.fc.dao.openfeign;

import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("shop-service")
public interface StoreDao {
    @GetMapping("/shops/{id}")
    InternalReturnObject<Store> getShopById(@PathVariable String id);
}
