package cn.edu.fc.service;

import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.mapper.po.StorePo;
import cn.edu.fc.service.dto.StoreDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    private final StoreDao storeDao;

    @Autowired
    public StoreService(StoreDao storeDao) {
        this.storeDao = storeDao;
    }

    public PageDto<StoreDto> retrieveStores(Integer page, Integer pageSize) {
        List<Store> preferences = this.storeDao.retrieveAll(page, pageSize);
        List<StoreDto> ret = preferences.stream().map(obj -> {
            StoreDto dto = StoreDto.builder().name(obj.getName()).address(obj.getAddress()).size(obj.getSize()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public StoreDto findStoreById(String storeId) {
        Store store = this.storeDao.findById(storeId);
        StoreDto ret = StoreDto.builder().name(store.getName()).address(store.getAddress()).size(store.getSize()).build();
        return ret;
    }

    public void createStore(String name, String address, Float size, UserDto user) {
        Store store = this.storeDao.findByNameAndAddress(name, address);
        if (null != store) {
            throw new BusinessException(ReturnNo.STORE_EXIST, String.format(ReturnNo.STORE_EXIST.getMessage(), store.getId()));
        }

        Store ret = Store.builder().name(name).address(address).size(size).build();
        this.storeDao.insert(store, user);
    }

    public void updateStore(String storeId, String name, String address, Float size, UserDto user) {
        Store store = this.storeDao.findByNameAndAddress(name, address);
        if (null == store) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), store.getId()));
        }

        store.setName(name);
        store.setAddress(address);
        store.setSize(size);
        this.storeDao.save(storeId, store, user);
    }

    public void deleteStore(String storeId) {
        Store store = this.storeDao.findById(storeId);
        if (null == store) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), store.getId()));
        }

        this.storeDao.delete(storeId);
    }
}
