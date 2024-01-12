package com.ictasl.java.projectinventory.Persistence.dao;
import java.util.List;
import com.ictasl.java.projectinventory.Persistence.entity.GovernmentServicesEntity;
import com.ictasl.java.projectinventory.Persistence.entity.MinistryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import java.util.Optional;

public interface InstituteManagementRepository extends JpaRepository<ManageInstituteEntity, Long> {
    @Query(value="SELECT * FROM institution WHERE ministry_id=:id", nativeQuery=true)
    List<ManageInstituteEntity> findByMinistryId(long id);

	Optional<ManageInstituteEntity> findByName(String name);
}
