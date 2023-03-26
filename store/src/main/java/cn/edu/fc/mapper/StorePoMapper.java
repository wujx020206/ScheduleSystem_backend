package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.StorePo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePoMapper extends JpaRepository<StorePo, Long> {
    StorePo findByNameAndAddress(String name, String address);
}
