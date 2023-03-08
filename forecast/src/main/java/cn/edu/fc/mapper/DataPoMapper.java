package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.DataPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DataPoMapper extends JpaRepository<DataPo, String> {
    Page<DataPo> findByStoreId(String storeId, Pageable pageable);

    Page<DataPo> findByStoreIdAndDateBetween(String storeId, String beginDate, String endDate, Pageable pageable);

    Page<DataPo> findByStoreIdAndDate(String storeId, String date, Pageable pageable);

    DataPo findByStoreIdAndDateAndBeginTimeAndEndTime(String storeId, String date, String beginTime, String endTime);
}
