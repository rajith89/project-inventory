package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.ExistingProjectRepository;
import com.ictasl.java.projectinventory.Persistence.dao.ExistingSolutionRepository;
import com.ictasl.java.projectinventory.Persistence.dao.InstituteManagementRepository;
import com.ictasl.java.projectinventory.Persistence.dao.SubProcurementRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ExistingProject;
import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import com.ictasl.java.projectinventory.Persistence.entity.SubProcurement;
import com.ictasl.java.projectinventory.Security.MyUserDetails;
import com.ictasl.java.projectinventory.Web.dto.ExistingProjectDto;
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

@Service("existingProjectService")
public class ExistingProjectServiceImpl implements ExistingProjectService{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExistingProjectRepository existingProjectRepository;

    @Autowired
    private ExistingSolutionRepository existingSolutionRepository;

    @Autowired
    private InstituteManagementRepository institutionRepository;

    @Autowired
    private SubProcurementRepository subProcurementRepository;


    @Override
    public ExistingProject createExistingProject() {
        return new ExistingProject();
    }

    @Override
    public ExistingProject saveExistingProject(ExistingProject existingProject) {
        MyUserDetails user = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ManageInstituteEntity> obj = institutionRepository.findById(user.getInstuteId());
        Optional<ExistingSolution> solObj = existingSolutionRepository.findById(existingProject.getExistingSolution().getId());
        existingProject.setInstitution(obj.get());
        existingProject.setExistingSolution(solObj.get());

        if(existingProject.getId() == null){
            for (SubProcurement subProcurement :existingProject.getSubProcurements()) {
                if(subProcurement.getStatus() == null){
                    subProcurement.setStatus(Status.Pending);
                }
            }
        }else{
            Optional<ExistingProject> project = existingProjectRepository.findById(existingProject.getId());

            if(project.isPresent()){
                ExistingProject projObj = project.get();
            }

            if(existingProject.getSubProcurements().isEmpty()){
                return existingProjectRepository.save(existingProject);
            }
            List<Long> subProcuremntIds= new ArrayList<>();
            for (SubProcurement subProcurement :existingProject.getSubProcurements()) {
                subProcuremntIds.add(subProcurement.getId());
            }
            subProcuremntIds.remove(null);

            if (subProcuremntIds.isEmpty()){
                for (SubProcurement subProcurement :existingProject.getSubProcurements()) {
                    if(subProcurement.getStatus() == null){
                        subProcurement.setStatus(Status.Pending);
                    }
                }
                return existingProjectRepository.save(existingProject);
            }else{
                List<SubProcurement>  subProcurementList = subProcurementRepository.getStatusBySubprocurementIds(subProcuremntIds);
                for (SubProcurement subProcurement :existingProject.getSubProcurements()) {
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
        return existingProjectRepository.save(existingProject);
    }

    @Override
    public void addSubProcurement(ExistingProject existingProject) {
        existingProject.getSubProcurements().add(new SubProcurement());
    }

    @Override
    public void removeSubProcurement(ExistingProject existingProject, Integer subprocurementIndex) {
        existingProject.getSubProcurements().remove(subprocurementIndex.intValue());
    }

    @Override
    public List<ExistingProject> findAllExistingProject() {
        // TODO Auto-generated method stub
        return existingProjectRepository.findAll();
    }

    @Override
    public List<ExistingProject> findAllExistingProjectByInstutionId(long id) {
        return existingProjectRepository.findAllById(id);
    }


    public ExistingProjectDto addExistingProject(ExistingProjectDto existingProjectDto) {
        LOGGER.info("add Existing Project");
        /*if its a delete opearation, delte the orders from patent order */
        if(!Boolean.TRUE.equals(existingProjectDto.getIsDelete())){
            ExistingProject existingProject = new ExistingProject();
            BeanUtils.copyProperties(existingProjectDto, existingProject);
            List<SubProcurement> subProcurementList = new ArrayList<>();
            if(existingProjectDto.getSubProcurementDtos() !=null && existingProjectDto.getSubProcurementDtos().size()>0){
                existingProjectDto.getSubProcurementDtos().forEach(subProcurementDto -> {
                    SubProcurement subProcurement= new SubProcurement();
                    BeanUtils.copyProperties(subProcurementDto,subProcurement);
                    //subProcurement.setExistingProject(existingProject);
                    subProcurementList.add(subProcurement);
                });
            }
            if(subProcurementList.size()>0){
                existingProject.setSubProcurements(subProcurementList);
            }
            existingProjectRepository.save(existingProject);
        } else {
            Optional<ExistingProject> oldExistingProjects = existingProjectRepository.findById(existingProjectDto.getId());
            existingProjectRepository.deleteById(oldExistingProjects.get().getId());
        }
        return null;
    }

}
