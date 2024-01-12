package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.InstituteManagementRepository;
import com.ictasl.java.projectinventory.Persistence.dao.NewProjectRepository;
import com.ictasl.java.projectinventory.Persistence.dao.SubProcurementRepository;
import com.ictasl.java.projectinventory.Persistence.entity.*;
import com.ictasl.java.projectinventory.Security.MyUserDetails;
import com.ictasl.java.projectinventory.Web.dto.NewProjectDto;
import com.ictasl.java.projectinventory.Web.dto.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("newProjectService")
public class NewProjectServiceImpl implements NewProjectService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private NewProjectRepository newProjectRepository;

    @Autowired
    private InstituteManagementRepository institutionRepository;

    @Autowired
    private SubProcurementRepository subProcurementRepository;


    @Override
    public NewProject createNewProject() {
        return new NewProject();
    }

    @Override
    public NewProject saveNewProject(NewProject newProject) {
        MyUserDetails user = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ManageInstituteEntity> obj = institutionRepository.findById(user.getInstuteId());
        newProject.setInstitution(obj.get());

        if(newProject.getId() == null){
            for (SubProcurement subProcurement :newProject.getSubProcurements()) {
                if(subProcurement.getStatus() == null){
                    subProcurement.setStatus(Status.Pending);
                }
            }
        }else{
            Optional<NewProject> project = newProjectRepository.findById(newProject.getId());

            if(project.isPresent()){
                NewProject projObj = project.get();
            }

            if(newProject.getSubProcurements().isEmpty()){
                return newProjectRepository.save(newProject);
            }
            List<Long> subProcuremntIds= new ArrayList<>();
            for (SubProcurement subProcurement :newProject.getSubProcurements()) {
                subProcuremntIds.add(subProcurement.getId());
            }
            subProcuremntIds.remove(null);

            if (subProcuremntIds.isEmpty()){
                for (SubProcurement subProcurement :newProject.getSubProcurements()) {
                    if(subProcurement.getStatus() == null){
                        subProcurement.setStatus(Status.Pending);
                    }
                }
                return newProjectRepository.save(newProject);
            }else{
                List<SubProcurement>  subProcurementList = subProcurementRepository.getStatusBySubprocurementIds(subProcuremntIds);
                for (SubProcurement subProcurement :newProject.getSubProcurements()) {
                    SubProcurement exisingSub = subProcurementList.stream()
                            .filter(sub -> (subProcurement.getId() == (sub.getId())))
                            .findAny()
                            .orElse(null);
                    if(exisingSub == null){
                        subProcurement.setStatus(Status.Pending);
                    }else{
                        subProcurement.setStatus(exisingSub.getStatus());
                    }
                }
            }
        }
        return newProjectRepository.save(newProject);
    }

    @Override
    public void addSubProcurement(NewProject newProject) {
        newProject.getSubProcurements().add(new SubProcurement());
    }

    @Override
    public void removeSubProcurement(NewProject newProject, Integer subprocurementIndex) {
        newProject.getSubProcurements().remove(subprocurementIndex.intValue());
    }

    @Override
    public List<NewProject> findAllNewProject() {
        // TODO Auto-generated method stub
        return newProjectRepository.findAll();
    }

    @Override
    public List<NewProject> findAllNewProjectByInstutionId(long id) {
        // TODO Auto-generated method stub
        return newProjectRepository.findAllById(id);
    }


    @Override
    public NewProjectDto addNewProject(NewProjectDto newProjectDto) {
        LOGGER.info("add Existing Project");
        /*if its a delete opearation, delte the orders from patent order */
        if(!Boolean.TRUE.equals(newProjectDto.getIsDelete())){
            NewProject newProject = new NewProject();
            BeanUtils.copyProperties(newProjectDto, newProject);
            List<SubProcurement> subProcurementList = new ArrayList<>();
            if(newProjectDto.getSubProcurementDtos() !=null && newProjectDto.getSubProcurementDtos().size()>0){
                newProjectDto.getSubProcurementDtos().forEach(subProcurementDto -> {
                    SubProcurement subProcurement= new SubProcurement();
                    BeanUtils.copyProperties(subProcurementDto,subProcurement);
                    subProcurementList.add(subProcurement);
                });
            }
            if(subProcurementList.size()>0){
                newProject.setSubProcurements(subProcurementList);
            }
            newProjectRepository.save(newProject);
        } else {
            Optional<NewProject> oldNewProjects = newProjectRepository.findById(newProjectDto.getId());
            newProjectRepository.deleteById(oldNewProjects.get().getId());
        }
        return null;
    }

}
