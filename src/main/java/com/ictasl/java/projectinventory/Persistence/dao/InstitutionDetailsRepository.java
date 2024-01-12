package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import com.ictasl.java.projectinventory.Persistence.entity.InstitutionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("insRepository")
public interface InstitutionDetailsRepository extends JpaRepository<InstitutionDetails, Long> {
    Optional<InstitutionDetails> findByInstituteId(long id);

}
