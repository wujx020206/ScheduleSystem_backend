package cn.edu.fc.controller;

import cn.edu.fc.controller.vo.CreatePreferenceVo;
import cn.edu.fc.controller.vo.StaffVo;
import cn.edu.fc.controller.vo.UpdatePreferenceVo;
import cn.edu.fc.javaee.core.aop.LoginUser;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.PreferenceService;
import cn.edu.fc.service.StaffService;
import cn.edu.fc.service.dto.PreferenceDto;
import cn.edu.fc.service.dto.StaffDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/staff", produces = "application/json;charset=UTF-8")
public class AdminStaffController {
    private final Logger logger = LoggerFactory.getLogger(AdminStaffController.class);

    private StaffService staffService;

    private PreferenceService preferenceService;

    @Autowired
    public AdminStaffController(PreferenceService preferenceService, StaffService staffService) {
        this.preferenceService = preferenceService;
        this.staffService = staffService;
    }

    @GetMapping("/staffs")
    public ReturnObject getStaffs(@RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<StaffDto> ret = this.staffService.retrieveStaffs(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{shopId}/staffs")
    public ReturnObject getShopStaffs(@PathVariable String shopId,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<StaffDto> ret = this.staffService.retrieveStaffsByShopId(shopId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{staffId}/staff")
    public ReturnObject findStaffById(@PathVariable String staffId) {
        StaffDto ret = this.staffService.findStaffById(staffId);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @PostMapping("/staff")
    public ReturnObject createStaff(@Valid @RequestBody StaffVo vo,
                                    @LoginUser UserDto user) {
        this.staffService.createStaff(vo.getName(), vo.getPosition(), vo.getPhone(), vo.getEmail(),vo.getShopId(), user);
        return new ReturnObject(ReturnNo.CREATED);
    }

    @PutMapping("{staffId}/staff")
    public ReturnObject updateStaff(@PathVariable String staffId,
                                    @Valid @RequestBody StaffVo vo,
                                    @LoginUser UserDto user) {
        this.staffService.updateStaff(staffId, vo.getName(), vo.getPosition(), vo.getPhone(), vo.getEmail(),vo.getShopId(), user);
        return new ReturnObject(ReturnNo.OK);
    }

    @DeleteMapping("{staffId}/staff")
    public ReturnObject deleteStaff(@PathVariable String staffId) {
        this.staffService.deleteStaff(staffId);
        return new ReturnObject(ReturnNo.OK);
    }

    @GetMapping("/preferences")
    public ReturnObject getPreferences(@RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<PreferenceDto> ret = this.preferenceService.retrievePreferences(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{staffId}/preferences")
    public ReturnObject getStaffPreferences(@PathVariable String staffId,
                                            @RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<PreferenceDto> ret = this.preferenceService.retrievePreferencesByStaffId(staffId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/preferencesp/{type}/preferences")
    public ReturnObject getPreferencesByType(@PathVariable Byte type,
                                             @RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<PreferenceDto> ret = this.preferenceService.retrievePreferencesByType(type, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{staffId}/preferences/{type}/preference")
    public ReturnObject getStaffPreferenceByType(@PathVariable String staffId,
                                                 @PathVariable Byte type) {
        PreferenceDto ret = this.preferenceService.retrievePreferencesByTypeAndStaffId(type, staffId);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @PostMapping("/{staffId}/preference")
    public ReturnObject createStaffPreference(@PathVariable String staffId,
                                              @Valid @RequestBody CreatePreferenceVo vo,
                                              @LoginUser UserDto user) {
        this.preferenceService.createPreference(staffId, vo.getType(), vo.getValue(), user);
        return new ReturnObject(ReturnNo.CREATED);
    }

    @PutMapping("/{staffId}/preferences/{preferenceId}/preference")
    public ReturnObject updateStaffPreference(@PathVariable String staffId,
                                              @PathVariable Byte type,
                                              @Valid @RequestBody UpdatePreferenceVo vo,
                                              @LoginUser UserDto user) {
        this.preferenceService.updatePreference(staffId, type, vo.getValue(), user);
        return new ReturnObject(ReturnNo.OK);
    }

    @DeleteMapping("/{staffId}/preferences/{preferenceId}/preference")
    public ReturnObject deleteStaffPreference(@PathVariable String staffId,
                                              @PathVariable Byte type) {
        this.preferenceService.deletePreference(type, staffId);
        return new ReturnObject(ReturnNo.OK);
    }
}
