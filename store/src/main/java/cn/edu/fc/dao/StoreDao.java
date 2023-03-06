package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.StorePoMapper;
import cn.edu.fc.mapper.po.StorePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.util.Common.putGmtFields;
import static cn.edu.fc.javaee.core.util.Common.putUserFields;

public class StoreDao {
    private final static Logger logger = LoggerFactory.getLogger(StoreDao.class);

    private final static String KEY = "E%s";

    @Value("3600")
    private int timeout;

    private StorePoMapper storePoMapper;

    private RedisUtil redisUtil;

    @Autowired
    public StoreDao(StorePoMapper storePoMapper) {
        this.storePoMapper = storePoMapper;
    }

    private Store getBo(StorePo po, Optional<String> redisKey) {
        Store bo = Store.builder().name(po.getName()).address(po.getAddress()).size(po.getSize()).build();
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private StorePo getPo(Store bo) {
        StorePo po = StorePo.builder().id(bo.getId()).name(bo.getName()).address(bo.getAddress()).size(bo.getSize()).build();
        return po;
    }

    public Store findById(String id) throws RuntimeException {
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);

        if (redisUtil.hasKey(key)) {
            Store bo = (Store) redisUtil.get(key);
            return bo;
        }

        Optional<StorePo> po = this.storePoMapper.findById(id);
        if (po.isPresent()) {
            return this.getBo(po.get(), Optional.of(key));
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工偏好", id));
        }
    }

    public List<Store> retrieveAll(Integer page, Integer pageSize) throws RuntimeException {
        List<StorePo> retList = this.storePoMapper.findAll(PageRequest.of(page, pageSize))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Store> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }

    public Store findByNameAndAddress(String name, String address) {
        StorePo po = this.storePoMapper.findByNameAndAddress(name, address);
        if (null == po) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), po.getId()));
        }

        return getBo(po, Optional.empty());
    }

    public String insert(Store store, UserDto user) throws RuntimeException {
        StorePo po = this.storePoMapper.findByNameAndAddress(store.getName(), store.getAddress());
        if (null == po) {
            StorePo storePo = getPo(store);
            putUserFields(storePo, "creator", user);
            putGmtFields(storePo, "create");
            this.storePoMapper.save(storePo);
            return storePo.getId();
        } else {
            throw new BusinessException(ReturnNo.STORE_EXIST, String.format(ReturnNo.STORE_EXIST.getMessage(), po.getId()));
        }
    }

    public String save(String storeId, Store store, UserDto user) {
        StorePo po = getPo(store);
        po.setId(storeId);
        if (null != user) {
            putUserFields(po, "modifier", user);
            putGmtFields(po, "modified");
        }
        this.storePoMapper.save(po);
        return String.format(KEY, store.getId());
    }

    public void delete(String id) {
        this.storePoMapper.deleteById(id);
    }
}
