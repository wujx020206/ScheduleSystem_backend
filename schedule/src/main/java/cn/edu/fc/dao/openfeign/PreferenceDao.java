package cn.edu.fc.dao.openfeign;

import cn.edu.fc.dao.bo.Preference;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("staff-service")
public interface PreferenceDao {
    @GetMapping("/staff/{staffId}/preferences/{type}/preference")
    InternalReturnObject<Preference<String>> getPreferenceByStaffIdAndType(Long staffId, Byte type);
}
