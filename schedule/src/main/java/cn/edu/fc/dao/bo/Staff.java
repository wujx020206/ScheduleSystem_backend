package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.openfeign.PreferenceDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.model.ReturnNo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Staff {
    static final Byte PREFERENCE_WORK_DAY = 0;
    static final Byte PREFERENCE_WORK_TIME = 1;
    static final Byte PREFERENCE_WORK_LONG = 2;

    private Long id;
    private String name;
    private String position;
    private String phone;
    private String email;
    private String shopName;

    @Setter
    private PreferenceDao preferenceDao;
    private List<Integer> workdayPreference;
    private Pair<Integer, Integer> workTimePreference;
    private Integer workLongPreference;

    public List<Integer> getWorkdayPreference() {
        if (workdayPreference != null)
            return workdayPreference;
        Preference<String> preference = getPreference(PREFERENCE_WORK_DAY);
        try {
            if (preference.getValue().strip().equals(""))
                return workdayPreference = new ArrayList<>();
            workdayPreference = Arrays.stream(preference.getValue().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "工作日偏好"));
        }
        return workdayPreference;
    }

    public Pair<Integer, Integer> getWorkTimePreference() {
        if (workTimePreference != null)
            return workTimePreference;
        Preference<String> preference = getPreference(PREFERENCE_WORK_TIME);
        try {
            if (preference.getValue().strip().equals(""))
                return workTimePreference = Pair.of(0, 24);
            String[] time = preference.getValue().split(" ");
            if (time.length != 2)
                throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "工作时间偏好"));
            workTimePreference = Pair.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        } catch (NumberFormatException | BusinessException e) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "工作时间偏好"));
        }
        return workTimePreference;
    }

    public Integer getWorkLongPreference() {
        if (workLongPreference != null)
            return workLongPreference;
        Preference<String> preference = getPreference(PREFERENCE_WORK_LONG);
        try {
            if (preference.getValue().strip().equals(""))
                return workLongPreference = 24;
            workLongPreference = Integer.parseInt(preference.getValue());
        } catch (NumberFormatException e) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "班次时长偏好"));
        }
        return workLongPreference;
    }

    private Preference<String> getPreference(Byte type) {
        if (preferenceDao == null)
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, ReturnNo.PARAMETER_MISSED.getMessage());
        InternalReturnObject<Preference<String>> preference = preferenceDao.getPreferenceByStaffIdAndType(id, type);
        if (preference.getErrno() != ReturnNo.OK.getErrNo())
            throw new BusinessException(ReturnNo.getByCode(preference.getErrno()), preference.getErrmsg());
        return preference.getData();
    }
}
