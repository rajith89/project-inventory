package com.ictasl.java.projectinventory.Persistence.dao;

import com.ictasl.java.projectinventory.Persistence.entity.FeedBack;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<FeedBack, Long> {
    @Query(value="SELECT * FROM feedback WHERE ins_id=:id", nativeQuery=true)
    List<FeedBack> findByInsId(long id);

}
