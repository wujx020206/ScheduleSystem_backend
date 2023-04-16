package cn.edu.fc.service;

import cn.edu.fc.dao.StaffScheduleDao;
import cn.edu.fc.dao.bo.*;
import cn.edu.fc.dao.openfeign.DataDao;
import cn.edu.fc.dao.openfeign.RuleDao;
import cn.edu.fc.dao.openfeign.StaffDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.scheduler.Scheduler;
import cn.edu.fc.service.dto.StaffScheduleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.model.Constants.MAX_RETURN;

@Service
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    private DataDao dataDao;
    private RuleDao ruleDao;
    private StaffDao staffDao;
    private StaffScheduleDao staffScheduleDao;
    private Scheduler scheduler;

    @Autowired
    public ScheduleService(DataDao dataDao, RuleDao ruleDao, StaffDao staffDao, StaffScheduleDao staffScheduleDao, Scheduler scheduler) {
        this.dataDao = dataDao;
        this.ruleDao = ruleDao;
        this.staffDao = staffDao;
        this.staffScheduleDao = staffScheduleDao;
        this.scheduler = scheduler;
    }

    private static StaffScheduleDto getDto(StaffSchedule bo) {
        return StaffScheduleDto.builder()
                .staffId(bo.getStaffId() == null ? null : bo.getStaffId())
                .staffName(bo.getStaffId() == null ? null : bo.getStaff().getName())
                .staffPosition(bo.getStaffId() == null ? null : bo.getStaff().getPosition())
                .startTime(bo.getStart())
                .endTime(bo.getEnd())
                .build();
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByDay(Long storeId, LocalDate date) {
        List<StaffSchedule> ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), 0, MAX_RETURN);
        if (null == ret || ret.isEmpty()) {
            logger.info("No schedule found for day {}, generating...", date);
            this.generateSchedule(storeId, date);
            ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), 0, MAX_RETURN);
        }
        List<StaffScheduleDto> dtos = ret.stream().map(ScheduleService::getDto).collect(Collectors.toList());
        return new PageDto<>(dtos, 0, dtos.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByDayAndSkill(Long storeId, LocalDate date, String skill) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(staff -> staff.getStaff() != null && staff.getStaff().getPosition().contains(skill))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByDayAndPosition(Long storeId, LocalDate date, String position) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(staff -> staff.getStaff() != null && staff.getStaff().getPosition().contains(position))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByDayAndStaff(Long storeId, LocalDate date, Long staffId) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(schedule -> schedule.getStaff() != null && schedule.getStaff().getId().equals(staffId))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByWeek(Long storeId, LocalDate date) {
        List<StaffSchedule> ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(7).atStartOfDay(), 0, MAX_RETURN);
        if (null == ret || ret.isEmpty()) {
            logger.info("No schedule found for week begin at {}, generating...", date);
            this.generateSchedule(storeId, date);
            ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(7).atStartOfDay(), 0, MAX_RETURN);
        }
        List<StaffScheduleDto> dtos = ret.stream().map(ScheduleService::getDto).collect(Collectors.toList());
        return new PageDto<>(dtos, 0, dtos.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByWeekAndSkill(Long storeId, LocalDate date, String skill) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByWeek(storeId, date)
                .getList()
                .stream()
                .filter(staff -> staff.getStaff() != null && staff.getStaff().getPosition().contains(skill))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByWeekAndPosition(Long storeId, LocalDate date, String position) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(staff -> staff.getStaff() != null && staff.getStaff().getPosition().contains(position))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByWeekAndStaff(Long storeId, LocalDate date, Long staffId) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByWeek(storeId, date)
                .getList()
                .stream()
                .filter(schedule -> schedule.getStaff() != null && schedule.getStaff().getId().equals(staffId))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public void updateStaffSchedule(Long storeId, Long id, String name) {
        Staff staff = this.staffDao.retrieveByName(name, 0, MAX_RETURN).getData().getList().get(0);
        StaffSchedule bo = this.staffScheduleDao.findById(id);
        if (null != staff) {
            StaffSchedule staffSchedule = StaffSchedule.builder().staffId(staff.getId()).start(bo.getStart()).end(bo.getEnd()).build();
            this.staffScheduleDao.save(id, staffSchedule);
        }
    }

    public Long findIdByStaffIdAndStartAndEnd(Long staffId, LocalDateTime start, LocalDateTime end) {
        Long ret = this.staffScheduleDao.findIdByStaffIdAndStartAndEnd(staffId, start, end);
        return ret;
    }

    private void generateSchedule(Long storeId, LocalDate date) {
        InternalReturnObject<PageDto<Data>> dataResp = dataDao.retrieveWeekData(storeId, date);
        List<Data> data = this.checkResponse(dataResp).getList();
        InternalReturnObject<PageDto<Staff>> staffResp = staffDao.retrieveAllStaffsByShopId(storeId);
        List<Staff> staffs = this.checkResponse(staffResp).getList();
        InternalReturnObject<List<String>> staffPositionResp = staffDao.retrieveAllStaffPositions();
        List<String> staffPositions = this.checkResponse(staffPositionResp);
        InternalReturnObject<AllRules> ruleResp = ruleDao.getAllRulesByStoreId(storeId);
        AllRules rules = this.checkResponse(ruleResp);

        ScheduleResult result = scheduler.schedule(data, staffs, staffPositions, date, rules);
        result.stream().forEach(schedule -> staffScheduleDao.insert(schedule));
    }

    private <T> T checkResponse(InternalReturnObject<T> resp) {
        if (resp.getErrno() != ReturnNo.OK.getErrNo())
            throw new BusinessException(ReturnNo.getByCode(resp.getErrno()), resp.getErrmsg());
        return resp.getData();
    }
}
