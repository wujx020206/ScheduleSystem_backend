package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.PreferencePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencePoMapper extends JpaRepository<PreferencePo, String> {
    Page<PreferencePo> findByStaffId(String staffId, Pageable pageable);

    Page<PreferencePo> findByType(Byte type, Pageable pageable);

    PreferencePo findByTypeAndStaffId(Byte type, String staffId);

    void deleteByTypeAndStaffId(Byte type, String staffId);
}
