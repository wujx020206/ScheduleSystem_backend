package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.openfeign.PreferenceDao;
import cn.edu.fc.dao.openfeign.StaffDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.bo.SSObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaffSchedule extends SSObject implements Serializable {
    @Getter
    @Setter
    private Long staffId;
    @ToString.Exclude
    @JsonIgnore
    @Setter
    private StaffDao staffDao;
    @Setter
    private Staff staff;
    @Getter
    @Setter
    private LocalDateTime start;
    @Getter
    @Setter
    private LocalDateTime end;
    // unit: Half an Hour
    @Getter
    @Setter
    private int duration;

    @Builder
    public StaffSchedule(Long staffId, LocalDateTime start, LocalDateTime end, int duration) {
        this.staffId = staffId;
        this.start = start;
        this.end = end;
        this.staff = null;
        this.duration = duration;
    }

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private PreferenceDao preferenceDao;

    public Staff getStaff() {
        if (staff != null)
            return staff;
        if (staffDao == null || staffId == null)
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, ReturnNo.PARAMETER_MISSED.getMessage());
        InternalReturnObject<Staff> response = staffDao.findStaffById(staffId);
        if (response.getErrno() != ReturnNo.OK.getErrNo())
            throw new BusinessException(ReturnNo.getByCode(response.getErrno()), response.getErrmsg());
        staff = response.getData();
        staff.setPreferenceDao(preferenceDao);
        return staff;
    }
}
