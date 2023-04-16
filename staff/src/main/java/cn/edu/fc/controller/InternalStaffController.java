package cn.edu.fc.controller;

import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.service.PreferenceService;
import cn.edu.fc.service.StaffService;
import cn.edu.fc.service.dto.PreferenceDto;
import cn.edu.fc.service.dto.StaffDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
public class InternalStaffController {
    private final Logger logger = LoggerFactory.getLogger(InternalStaffController.class);

    private final StaffService staffService;

    private final PreferenceService preferenceService;

    @Autowired
    public InternalStaffController(PreferenceService preferenceService, StaffService staffService) {
        this.preferenceService = preferenceService;
        this.staffService = staffService;
    }

    @GetMapping("/{staffId}/preferences/{type}/preference")
    public ReturnObject getStaffPreferenceByType(@PathVariable Long staffId,
                                                 @PathVariable Byte type) {
        PreferenceDto ret = this.preferenceService.retrievePreferencesByTypeAndStaffId(type, staffId);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/name/{staffName}/staff")
    public ReturnObject findStaffByName(@PathVariable String staffName,
                                        @RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<StaffDto> ret = this.staffService.retrieveStaffByName(staffName, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/staff/positions")
    public ReturnObject getPositions() {
        List<String> ret = this.staffService.retrievePositions();
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/staff/skills")
    public ReturnObject getSkills() {
        List<String> ret = this.staffService.retrieveSkills();
        return new ReturnObject(ReturnNo.OK, ret);
    }
}
