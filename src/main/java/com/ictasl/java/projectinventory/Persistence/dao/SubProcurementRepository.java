package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.SubProcurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubProcurementRepository extends JpaRepository<SubProcurement, Long> {

    @Query(value="SELECT * FROM sub_procurement WHERE exproj_id=:ex_proj_id", nativeQuery=true)
    List<SubProcurement> getSubProcurementByExistingProjectId(@Param("ex_proj_id")Long ex_proj_id);

    @Query(value="SELECT * FROM sub_procurement WHERE new_proj_id=:proj_id", nativeQuery=true)
    List<SubProcurement> getSubProcurementByNewProjectId(@Param("proj_id")Long proj_id);

    @Query("SELECT b FROM SubProcurement b WHERE b.id IN ?1")
    List<SubProcurement> getStatusBySubprocurementIds(List<Long> subprocurementsIds);






}
