package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.GovernmentServicesEntity;
import com.ictasl.java.projectinventory.Persistence.entity.InstitutionDetails;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GovernmentServicesRepository extends CrudRepository<GovernmentServicesEntity, Long> {
    @Query(value="SELECT * FROM government_services WHERE ins_id=:id", nativeQuery=true)
    Optional<GovernmentServicesEntity> findByInstituteId(long id);
}
