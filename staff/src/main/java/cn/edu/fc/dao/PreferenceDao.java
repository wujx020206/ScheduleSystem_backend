package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Preference;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.PreferencePoMapper;
import cn.edu.fc.mapper.po.PreferencePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RefreshScope
public class PreferenceDao {
    private final static Logger logger = LoggerFactory.getLogger(PreferenceDao.class);

    private final static String KEY = "E%d";

    @Value("3600")
    private int timeout;

    private PreferencePoMapper preferencePoMapper;

    private RedisUtil redisUtil;

    private StaffDao staffDao;

    @Autowired
    public PreferenceDao(PreferencePoMapper preferencePoMapper, RedisUtil redisUtil, StaffDao staffDao) {
        this.preferencePoMapper = preferencePoMapper;
        this.redisUtil = redisUtil;
        this.staffDao = staffDao;
    }

    private Preference getBo(PreferencePo po, Optional<String> redisKey) {
        Preference bo = Preference.builder().type(po.getType()).staffId(po.getStaffId()).value(po.getValue()).build();
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(Preference bo) {
        bo.setStaffDao(bo.getStaffDao());
    }
}
