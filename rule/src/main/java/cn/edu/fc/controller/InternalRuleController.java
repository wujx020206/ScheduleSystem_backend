package cn.edu.fc.controller;

import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.service.RuleService;
import cn.edu.fc.service.dto.RuleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
public class InternalRuleController {
    private final Logger logger = LoggerFactory.getLogger(InternalRuleController.class);

    private final RuleService ruleService;

    @Autowired
    public InternalRuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping("/rules")
    public ReturnObject getRules(@RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<RuleDto> ret = this.ruleService.retrieveRules(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{storeId}/rules")
    public ReturnObject getStoreRules(@PathVariable String storeId,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<RuleDto> ret = this.ruleService.retrieveRulesByStoreId(storeId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }
}
