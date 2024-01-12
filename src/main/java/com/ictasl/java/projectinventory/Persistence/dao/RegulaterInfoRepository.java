package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import com.ictasl.java.projectinventory.Persistence.entity.RegulaterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("regulaterInfoRepository")
public interface RegulaterInfoRepository extends JpaRepository<RegulaterInfo, Long> {

    @Query(value="SELECT * FROM regulater_info WHERE ins_id=:insId ORDER BY regulation_id ASC ", nativeQuery=true)
    List<RegulaterInfo> findAllById(@Param("insId") Long insId);
}
