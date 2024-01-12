package com.ictasl.java.projectinventory.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ictasl.java.projectinventory.Persistence.dao.InstituteManagementRepository;
import com.ictasl.java.projectinventory.Persistence.dao.MinistryRepository;
import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import com.ictasl.java.projectinventory.Persistence.entity.MinistryEntity;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Web.dto.InstituteManageDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;

@Service
public class InstituteManageServiceImpl implements InstituteManageService{

	@Autowired
	private MinistryRepository ministryRepository;
	
	@Autowired
	private InstituteManagementRepository instituteManagementRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private User getLoggedUserDetails(Principal principal) throws Exception{
		User user = userRepository.findByUserName(principal.getName());
		if(user == null) {
			throw new RuntimeException("No logged user");
		}
		return user;
	}
	
	@Override
	public List<HashMap<String, String>> getMinistryNameList() throws Exception {
		List<MinistryEntity> ministryList = ministryRepository.findAllByStatus("Y");
				
		 List<HashMap<String, String>> returnList = ministryList.stream().map(m->{
			HashMap<String,String> map = new HashMap<>();
			map.put("name", m.getName());
			map.put("id", Long.toString(m.getId()));
			
			return map;
		}).collect(Collectors.toList());
		 System.out.println(returnList.size());
		return returnList;
	}

	@Override
	public void createInstitute(InstituteManageDto institueManageDto,  Principal principal) throws Exception {
		User user = getLoggedUserDetails(principal);
		Optional<ManageInstituteEntity> manageInstituteEntityExisting = instituteManagementRepository.findByName(institueManageDto.getName());
		if(manageInstituteEntityExisting.isPresent()) {
			throw new RuntimeException("Institute name already existing");
		}
		ManageInstituteEntity manageInstituteEntity = new ManageInstituteEntity();
		InstituteManageDto institueManageDtoReturn = new InstituteManageDto();
		BeanUtils.copyProperties(institueManageDto, manageInstituteEntity);
		
		//manageInstituteEntity.setUser(user);
		
		MinistryEntity ministryEntity = new MinistryEntity();
		ministryEntity.setId(institueManageDto.getMinistryId());
		
		manageInstituteEntity.setMinistryId(ministryEntity);
		
		instituteManagementRepository.save(manageInstituteEntity);
		//BeanUtils.copyProperties(ministryEntity, ministryDtoReturn);
		
		//return ministryDtoReturn;
	}

	@Override
	public InstituteManageDto editInstitute(long id) throws Exception{

		InstituteManageDto institueManageDtoReturn = new InstituteManageDto();

		Optional<ManageInstituteEntity> manageInstituteEntity = instituteManagementRepository.findById(id);
		if (manageInstituteEntity.isPresent()) {
			BeanUtils.copyProperties(manageInstituteEntity.get(), institueManageDtoReturn);
			if(manageInstituteEntity.get().getMinistryId() != null) {
				institueManageDtoReturn.setMinistryId(manageInstituteEntity.get().getMinistryId().getId());
				institueManageDtoReturn.setMinistryName(manageInstituteEntity.get().getMinistryId().getName());
			}
			//institueManageDtoReturn.setIntituteName(manageInstituteEntity.get().getMinistryId().getName());
		}
		return institueManageDtoReturn;
	}

	@Override
	public void update(long id,InstituteManageDto institueManageDto , Principal principal) throws Exception{
		User user = getLoggedUserDetails(principal);
		InstituteManageDto institueManageDtoReturn = new InstituteManageDto();
		ManageInstituteEntity savinObj = new ManageInstituteEntity();
		Optional<ManageInstituteEntity> manageInstituteEntity = instituteManagementRepository.findById(id);
		if (manageInstituteEntity.isPresent()) {
			Optional<ManageInstituteEntity> manageInstituteByName = instituteManagementRepository.findByName(institueManageDto.getName());
			if(manageInstituteByName.isPresent() && manageInstituteByName.get().getId() != id) {
				throw new RuntimeException("Institute name already existing");
			}
			BeanUtils.copyProperties(institueManageDto, savinObj);
			MinistryEntity ministryEntity = new MinistryEntity();
			ministryEntity.setId(institueManageDto.getMinistryId());
			savinObj.setMinistryId(ministryEntity);
			//savinObj.setUser(user);
			instituteManagementRepository.save(savinObj);
			
		}
	}

	@Override
	public List<InstituteManageDto> getAllSavedList() throws Exception{
		List<ManageInstituteEntity> instituteList = (List<ManageInstituteEntity>) instituteManagementRepository.findAll();
		List<InstituteManageDto> instituteDtoReturnList = new ArrayList<>();
		if(!instituteList.isEmpty()) {
			instituteDtoReturnList = instituteList.stream().map(p -> {
				InstituteManageDto instituteDtoReturn = new InstituteManageDto();
				BeanUtils.copyProperties(p, instituteDtoReturn);
				if(p.getMinistryId() != null) {
					instituteDtoReturn.setMinistryId(p.getMinistryId().getId());
					instituteDtoReturn.setMinistryName(p.getMinistryId().getName());
				}
				return instituteDtoReturn;
			}).collect(Collectors.toList());
		}
		System.out.println(instituteDtoReturnList.size());
		return instituteDtoReturnList;
	}

	@Override
	public void delete(long id) throws Exception{
		Optional<ManageInstituteEntity> existingInstitute = instituteManagementRepository.findById(id);
		if (existingInstitute.isPresent()) {
			instituteManagementRepository.deleteById(id);
		}
	}

	@Override
	public List<HashMap<String, String>> getInsNameList(){
		List<ManageInstituteEntity> ministryList = (List<ManageInstituteEntity>) instituteManagementRepository.findAll();

		List<HashMap<String, String>> returnList = ministryList.stream().map(m->{
			HashMap<String,String> map = new HashMap<>();
			map.put("name", m.getName());
			map.put("id", Long.toString(m.getId()));

			return map;
		}).collect(Collectors.toList());
		return returnList;
	}

	@Override
	public InstituteManageDto getInstitution(Long id) throws ResourceNotFoundException {
		Optional<ManageInstituteEntity> institutionOptional = instituteManagementRepository.findById(id);
		if (!institutionOptional.isPresent()) {
			throw new ResourceNotFoundException("Institution Solution not found.");
		}
		ManageInstituteEntity institution = institutionOptional.get();
		InstituteManageDto institutionDTO = new InstituteManageDto();
		BeanUtils.copyProperties(institution,institutionDTO);
		return institutionDTO;
	}

	@Override
	public List<HashMap<String, String>> getAllInsByMinId(long minId) throws ResourceNotFoundException {
		List<ManageInstituteEntity> instituteList = (List<ManageInstituteEntity>) instituteManagementRepository.findByMinistryId(minId);

		List<HashMap<String, String>> returnList = instituteList.stream().map(m -> {
			HashMap<String, String> map = new HashMap<>();
			map.put("name", m.getName());
			map.put("id", Long.toString(m.getId()));

			return map;
		}).collect(Collectors.toList());
		return returnList;

	}

	@Override
	public Page<ManageInstituteEntity> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo,pageSize);
		return this.instituteManagementRepository.findAll(pageable);
	}
}

