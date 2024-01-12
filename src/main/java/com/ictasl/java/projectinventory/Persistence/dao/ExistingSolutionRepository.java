package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("existingSolutionRepository")
public interface ExistingSolutionRepository extends JpaRepository<ExistingSolution, Long> {

//    @Query("SELECT S FROM ExistingSolution S WHERE  ORDER BY S.id ASC")
    @Query(value="SELECT * FROM existing_solution WHERE ins_id=:insId ORDER BY exsol_id ASC ", nativeQuery=true)
    List<ExistingSolution> findAllById(@Param("insId") Long insId);
}
