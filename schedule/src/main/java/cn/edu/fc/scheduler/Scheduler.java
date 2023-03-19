package cn.edu.fc.scheduler;

import cn.edu.fc.dao.bo.AllRules;
import cn.edu.fc.dao.bo.Data;
import cn.edu.fc.dao.bo.Staff;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Scheduler {
    private final static int MAX_RANDOM_TIMES = 5;
    private final Random random;

    public Scheduler() {
        random = new Random();
    }

    public WeekResult schedule(List<Data> data, List<Staff> staffs, List<String> staffPositions, AllRules rules) {
        HashMap<String, Queue<StaffScheduleInternal>> staffByPosition = this.convertStaffsToMap(staffs, staffPositions);
        // todo
        return null;
    }
    // 填充员工
    private DayResult applyStaffToSchedule(DayResult dayResult, HashMap<String, Queue<StaffScheduleInternal>> staffs, AllRules rules) {
        // todo
        return null;
    }
    // 创建空时间表
    private DayResult createEmptySchedule(List<Integer> needStaffs, AllRules rules) {
        // todo
        return null;
    }
    private void recycleStaff(StaffScheduleInternal staff, HashMap<String, Queue<StaffScheduleInternal>> allStaffs) {
        allStaffs.get(staff.getStaff().getPosition()).add(staff);
    }
    private StaffScheduleInternal getStaffWithPositions(List<String> allowedPositions, HashMap<String, Queue<StaffScheduleInternal>> allStaffs, Function<StaffScheduleInternal, Boolean> validate) {
        String randomPosition;
        int cnt = 0;
        do {
            if (cnt++ > MAX_RANDOM_TIMES)
                return null;
            randomPosition = allowedPositions.get(random.nextInt(allowedPositions.size()));
        } while(allStaffs.get(randomPosition).isEmpty() && !validate.apply(allStaffs.get(randomPosition).peek()));
        return allStaffs.get(randomPosition).poll();
    }
    // 将按时间区间分配的人数转换为按时间点分配的人数
    private List<Integer> calculateNeedStaffs(List<Data> data, boolean isWeekDay, AllRules rules) {
        Integer openTime = isWeekDay ? rules.getWeekDayOpenRule() : rules.getWeekendOpenRule();
        Integer closeTime = isWeekDay ? rules.getWeekDayCloseRule() : rules.getWeekendCloseRule();
        List<Integer> needStaffs = new ArrayList<>(Arrays.asList(new Integer[48]));
        for (int i = 0; i < rules.getPrepareTime(); ++i)
            needStaffs.set(openTime - i - 1, rules.getPreparePeople());
        for (int i = 0; i < rules.getEndTime(); ++i)
            needStaffs.set(closeTime + i, rules.getEndPeople());
        data.forEach(d -> {
            int beginTime = this.timeToId(d.getBeginTime());
            int endTime = this.timeToId(d.getEndTime());
            int num = (int)Math.ceil(d.getNum() / rules.getWorkPeople());
            for (int i = beginTime; i < endTime; i++) {
                needStaffs.set(i, num);
            }
        });
        return needStaffs.stream().map(count -> count < rules.getLeastPeople() ? rules.getLeastPeople() : count).collect(Collectors.toList());
    }
    private int timeToId(String time) {
        String[] timeArray = time.split(":");
        return Integer.parseInt(timeArray[0]) * 2 + (Integer.parseInt(timeArray[1]) == 30 ? 1 : 0);
    }
    private HashMap<String, Queue<StaffScheduleInternal>> convertStaffsToMap(List<Staff> staffs, List<String> staffPositions) {
        HashMap<String, Queue<StaffScheduleInternal>> staffByPosition = new HashMap<>();
        staffPositions.forEach(position -> staffByPosition.put(position, new LinkedList<>()));
        Collections.shuffle(staffs);
        staffs.stream()
                .map(staff -> new StaffScheduleInternal(staff, null, null, null, 0, 0))
                .forEach(staff -> staffByPosition.get(staff.getStaff().getPosition()).add(staff));
        return staffByPosition;
    }
}

@lombok.Data
@AllArgsConstructor
class StaffScheduleInternal {
    private Staff staff;
    private Integer lastWorkedDay;
    private Integer lastWorkedHourStart;
    private Integer lastWorkedHourEnd;
    private Integer dayWorkedTime;
    private Integer weekWorkedTime;
}
