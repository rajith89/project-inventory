package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.entity.ExistingProject;

import java.util.List;

public interface ExistingProjectService {

    ExistingProject createExistingProject();
    ExistingProject saveExistingProject(ExistingProject existingProject);
    void addSubProcurement(ExistingProject existingProject);
    void removeSubProcurement(ExistingProject existingProject, Integer subprocurementIndex);
    List<ExistingProject> findAllExistingProject();
    List<ExistingProject> findAllExistingProjectByInstutionId(long id);



}
