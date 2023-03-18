package cn.edu.fc.scheduler;

import cn.edu.fc.dao.bo.AllRules;
import cn.edu.fc.dao.bo.Data;
import cn.edu.fc.dao.bo.Staff;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Scheduler {
    public WeekResult schedule(List<Data> data, List<Staff> staffs, List<String> staffPositions, AllRules rules) {
        HashMap<String, Queue<Staff>> staffByPosition = this.convertStaffsToMap(staffs, staffPositions);
        return null;
    }
    // 将按时间区间分配的人数转换为按时间点分配的人数
    private List<Integer> calculateNeedStaffs(List<Data> data, AllRules rules) {
        List<Integer> needStaffs = new ArrayList<>(Arrays.asList(new Integer[48]));
        data.forEach(d -> {
            int beginTime = this.timeToId(d.getBeginTime());
            int endTime = this.timeToId(d.getEndTime());
            int num = (int)Math.ceil(d.getNum() / rules.getWorkPeople());
            for (int i = beginTime; i < endTime; i++) {
                needStaffs.set(i, num);
            }
        });
        return needStaffs;
    }
    private int timeToId(String time) {
        String[] timeArray = time.split(":");
        return Integer.parseInt(timeArray[0]) * 2 + (Integer.parseInt(timeArray[1]) == 30 ? 1 : 0);
    }
    private HashMap<String, Queue<Staff>> convertStaffsToMap(List<Staff> staffs, List<String> staffPositions) {
        HashMap<String, Queue<Staff>> staffByPosition = new HashMap<>();
        staffPositions.forEach(position -> staffByPosition.put(position, new LinkedList<>()));
        Collections.shuffle(staffs);
        staffs.forEach(staff -> staffByPosition.get(staff.getPosition()).add(staff));
        return staffByPosition;
    }
}
