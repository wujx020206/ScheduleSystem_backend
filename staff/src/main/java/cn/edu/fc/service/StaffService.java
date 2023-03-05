package cn.edu.fc.service;

import cn.edu.fc.dao.PreferenceDao;
import cn.edu.fc.dao.StaffDao;
import cn.edu.fc.dao.bo.Shop;
import cn.edu.fc.dao.bo.Staff;
import cn.edu.fc.dao.openfeign.ShopDao;
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

    private PreferenceDao preferenceDao;

    private StaffDao staffDao;

    private ShopDao shopDao;

    @Autowired
    public StaffService(PreferenceDao preferenceDao, StaffDao staffDao) {
        this.preferenceDao = preferenceDao;
        this.staffDao = staffDao;
    }

    public PageDto<StaffDto> retrieveStaffs(Integer page, Integer pageSize) {
        List<Staff> staffs = this.staffDao.retrieveAll(page, pageSize);
        List<StaffDto> ret = staffs.stream().map(obj -> {
            StaffDto dto = StaffDto.builder().name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getShop().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<StaffDto> retrieveStaffsByShopId(String shopId, Integer page, Integer pageSize) {
        List<Staff> staffs = this.staffDao.retrieveByShopId(shopId, page, pageSize);
        List<StaffDto> ret = staffs.stream().map(obj -> {
            StaffDto dto = StaffDto.builder().name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getShop().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public StaffDto findStaffById(String staffId) {
        Staff obj = this.staffDao.findById(staffId);
        StaffDto dto = StaffDto.builder().name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getShop().getName()).build();
        return dto;
    }

    public void createStaff(String name, String position, String phone, String email, String shopId, UserDto user) {
        Shop shop = this.shopDao.getShopById(shopId).getData();
        if (null == shop) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺", shopId));
        }

        Staff staff = Staff.builder().name(name).position(position).phone(phone).email(email).shopId(shopId).build();
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
        staff.setShopId(shopId);
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
