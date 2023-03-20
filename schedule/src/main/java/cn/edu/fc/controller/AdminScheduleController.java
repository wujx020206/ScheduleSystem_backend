package cn.edu.fc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/schedule", produces = "application/json;charset=UTF-8")
public class AdminScheduleController {
    private final Logger logger = LoggerFactory.getLogger(AdminScheduleController.class);
}

