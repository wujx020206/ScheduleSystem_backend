package cn.edu.fc.service;

import cn.edu.fc.dao.StaffDao;
import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.dao.bo.Staff;
import cn.edu.fc.dao.openfeign.StoreDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.dto.StaffDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {
    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);

    private final StaffDao staffDao;

    private final StoreDao storeDao;

    @Autowired
    public StaffService(StaffDao staffDao, StoreDao storeDao) {
        this.staffDao = staffDao;
        this.storeDao = storeDao;
    }

    public PageDto<StaffDto> retrieveStaffs(Integer page, Integer pageSize) {
        List<Staff> staffs = this.staffDao.retrieveAll(page, pageSize);
        List<StaffDto> ret = staffs.stream().map(obj -> {
            StaffDto dto = StaffDto.builder().name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<StaffDto> retrieveStaffsByStoreId(String storeId, Integer page, Integer pageSize) {
        List<Staff> staffs = this.staffDao.retrieveByShopId(storeId, page, pageSize);
        List<StaffDto> ret = staffs.stream().map(obj -> {
            StaffDto dto = StaffDto.builder().name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public StaffDto findStaffById(String staffId) {
        Staff obj = this.staffDao.findById(staffId);
        StaffDto dto = StaffDto.builder().name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getStore().getName()).build();
        return dto;
    }

    public void createStaff(String name, String position, String phone, String email, String shopId, UserDto user) {
        Store shop = this.storeDao.getStoreById(shopId).getData();
        if (null == shop) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺", shopId));
        }

        Staff staff = Staff.builder().name(name).position(position).phone(phone).email(email).storeId(shopId).build();
        this.staffDao.insert(staff, user);
    }

    public void updateStaff(String staffId, String name, String position, String phone, String email, String shopId, UserDto user) {
        Staff staff = this.staffDao.findById(staffId);
        if (null == staff) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工", staffId));
        }

        staff.setName(name);
        staff.setPosition(position);
        staff.setPhone(phone);
        staff.setEmail(email);
        staff.setStoreId(shopId);
        this.staffDao.save(staffId, staff, user);
    }

    public void deleteStaff(String staffId) {
        Staff staff = this.staffDao.findById(staffId);
        if (null == staff) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工", staffId));
        }

        this.staffDao.delete(staffId);
    }
}
