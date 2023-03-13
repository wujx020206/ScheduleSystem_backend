package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Rule;
import cn.edu.fc.dao.openfeign.StoreDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.RulePoMapper;
import cn.edu.fc.mapper.po.RulePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

public class RuleDao {
    private final static Logger logger = LoggerFactory.getLogger(RuleDao.class);

    private final static String KEY = "E%s";

    @Value("3600")
    private int timeout;

    private RulePoMapper rulePoMapper;

    private RedisUtil redisUtil;

    private StoreDao storeDao;

    @Autowired
    public RuleDao(RulePoMapper rulePoMapper, RedisUtil redisUtil, StoreDao storeDao) {
        this.rulePoMapper = rulePoMapper;
        this.redisUtil = redisUtil;
        this.storeDao = storeDao;
    }

    private Rule getBo(RulePo po, Optional<String> redisKey) {
        Rule bo = Rule.builder().type(po.getType()).value(po.getValue()).storeId(po.getStoreId()).build();
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(Rule bo) {
        bo.setStoreDao(storeDao);
    }

    private RulePo getPo(Rule bo) {
        RulePo po = RulePo.builder().id(bo.getId()).type(bo.getType()).value(bo.getValue()).storeId(bo.getStoreId()).build();
        return po;
    }

    public Rule findById(String id) throws RuntimeException {
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);

        if (redisUtil.hasKey(key)) {
            Rule bo = (Rule) redisUtil.get(key);
            this.setBo(bo);
            return bo;
        }

        Optional<RulePo> po = this.rulePoMapper.findById(id);
        if (po.isPresent()) {
            return this.getBo(po.get(), Optional.of(key));
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工偏好", id));
        }
    }
}
