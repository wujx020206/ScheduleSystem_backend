package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.RulePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RulePoMapper extends JpaRepository<RulePo, String> {
    Page<RulePo> findByStoreId(String storeId, Pageable pageable);

    RulePo findByTypeAndStoreId(String type, String storeId);
}
