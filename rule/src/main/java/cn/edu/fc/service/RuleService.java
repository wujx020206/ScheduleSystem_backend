package cn.edu.fc.service;

import cn.edu.fc.dao.RuleDao;
import cn.edu.fc.dao.bo.Rule;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.service.dto.RuleDto;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleService {
    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

    private final RuleDao ruleDao;

    @Autowired
    public RuleService(RuleDao ruleDao) {
        this.ruleDao = ruleDao;
    }

    public PageDto<RuleDto> retrieveRules(Integer page, Integer pageSize) {
        List<Rule> preferences = this.ruleDao.retrieveAll(page, pageSize);
        List<RuleDto> ret = preferences.stream().map(obj -> {
            String[] arr = obj.getType().split("_");
            RuleDto dto = RuleDto.builder().firstType(arr[0]).secondType(arr[1]).value(obj.getValue()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<RuleDto> retrieveRulesByStoreId(String storeId, Integer page, Integer pageSize) {
        List<Rule> preferences1 = this.ruleDao.retrieveByStoreId(storeId, page, pageSize);
        List<Rule> preferences2 = this.ruleDao.retrieveByStoreId(null, page, pageSize);
        List<Rule> preferences = Lists.newArrayList();
        preferences.addAll(preferences1);
        preferences.addAll(preferences2);

        List<RuleDto> ret = preferences.stream().map(obj -> {
            String[] arr = obj.getType().split("_");
            RuleDto dto = RuleDto.builder().firstType(arr[0]).secondType(arr[1]).value(obj.getValue()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }


}
