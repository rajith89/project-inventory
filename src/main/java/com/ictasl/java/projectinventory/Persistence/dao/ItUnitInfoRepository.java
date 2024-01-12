package com.ictasl.java.projectinventory.Persistence.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ictasl.java.projectinventory.Persistence.entity.ItUnitInfomationEntity;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;

public interface ItUnitInfoRepository extends CrudRepository<ItUnitInfomationEntity, Long> {

	Optional<ItUnitInfomationEntity> findByInstitution(ManageInstituteEntity manageInstituteEntity);


}
