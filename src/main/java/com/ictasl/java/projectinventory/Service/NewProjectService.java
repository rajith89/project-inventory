package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.entity.NewProject;
import com.ictasl.java.projectinventory.Web.dto.NewProjectDto;

import java.util.List;

public interface NewProjectService {
    NewProject createNewProject();

    NewProject saveNewProject(NewProject newProject);

    void addSubProcurement(NewProject newProject);

    void removeSubProcurement(NewProject newProject, Integer subprocurementIndex);

    List<NewProject> findAllNewProject();

    List<NewProject> findAllNewProjectByInstutionId(long id);

    NewProjectDto addNewProject(NewProjectDto newProjectDto);
}
