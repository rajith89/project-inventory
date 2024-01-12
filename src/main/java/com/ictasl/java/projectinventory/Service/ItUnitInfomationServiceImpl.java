package com.ictasl.java.projectinventory.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ictasl.java.projectinventory.Persistence.dao.ItTeamClassificationRepository;
import com.ictasl.java.projectinventory.Persistence.dao.ItUnitInfoRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ItTeamClassificationEntity;
import com.ictasl.java.projectinventory.Persistence.entity.ItUnitInfomationEntity;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import com.ictasl.java.projectinventory.Web.dto.ItTeamClassificationDto;
import com.ictasl.java.projectinventory.Web.dto.ItUnitInfomationDto;

@Service
public class ItUnitInfomationServiceImpl implements ItUnitInfomationService{
	
	@Autowired
	private ItUnitInfoRepository itUnitInfoRepository;

	@Autowired
	private ItTeamClassificationRepository classificationRepository;

	@Override
	public ItUnitInfomationDto getItUnitInforInOrg(long orgId) throws Exception {
		ManageInstituteEntity manageInstituteEntity = new ManageInstituteEntity();
		manageInstituteEntity.setId(orgId);
		Optional<ItUnitInfomationEntity> itUnitInfoEntity = itUnitInfoRepository.findByInstitution(manageInstituteEntity);
		ItUnitInfomationDto itUnitInfomationDtoReturn = returnMappedDto();

		if (itUnitInfoEntity.isPresent()) {
			itUnitInfomationDtoReturn = bindEntityToDto(itUnitInfoEntity.get());
		}

		return itUnitInfomationDtoReturn;
	}
	
	private ItUnitInfomationDto returnMappedDto() {

		ItUnitInfomationDto itUnitInfomationDto = new ItUnitInfomationDto();

		List<ItTeamClassificationDto> itTeamClassificationDtoList = new ArrayList<>();
		itUnitInfomationDto.setItTeamClassificationList(itTeamClassificationDtoList);
		return itUnitInfomationDto;
	}

	private ItUnitInfomationDto bindEntityToDto(ItUnitInfomationEntity itUnitInfomationEntity) {
		ItUnitInfomationDto itUnitInfomationDto = new ItUnitInfomationDto();
		List<ItTeamClassificationDto> itTeamClassificationDtoList = new ArrayList<>();

		if (!itUnitInfomationEntity.getItTeamClassificationList().isEmpty()) {
			for (ItTeamClassificationEntity entity : itUnitInfomationEntity.getItTeamClassificationList()) {
				ItTeamClassificationDto itTeamClassificationDto = new ItTeamClassificationDto();
				BeanUtils.copyProperties(entity, itTeamClassificationDto);
				itTeamClassificationDtoList.add(itTeamClassificationDto);
			}
		}
		BeanUtils.copyProperties(itUnitInfomationEntity, itUnitInfomationDto);
		itUnitInfomationDto.setItTeamClassificationList(itTeamClassificationDtoList);
		return itUnitInfomationDto;
	}
	
	private ItUnitInfomationEntity bindDtoToEntity(ItUnitInfomationDto itUnitInfomationDto, long orgId) {
		ItUnitInfomationEntity itUnitInfomationEntity = new ItUnitInfomationEntity();
		List<ItTeamClassificationEntity> itTeamClassificationEntityList = new ArrayList<>();
		if (!itUnitInfomationDto.getItTeamClassificationList().isEmpty()) {
			for (ItTeamClassificationDto entity : itUnitInfomationDto.getItTeamClassificationList()) {
				ItTeamClassificationEntity itTeamClassification = new ItTeamClassificationEntity();
				BeanUtils.copyProperties(entity, itTeamClassification);
				itTeamClassificationEntityList.add(itTeamClassification);
			}
			BeanUtils.copyProperties(itUnitInfomationDto, itUnitInfomationEntity);
		}

		BeanUtils.copyProperties(itUnitInfomationDto, itUnitInfomationEntity);
		//User user = getLoggedUserDetails(principal);
		ManageInstituteEntity manageInstituteEntity = new ManageInstituteEntity();
		manageInstituteEntity.setId(orgId);
		return itUnitInfomationEntity;
	}
	
	private boolean isPresentExistingOne(long id) {
		Optional<ItUnitInfomationEntity> existingObj = itUnitInfoRepository.findById(id);
		return existingObj.isPresent();
	}

	@Override
	public ItUnitInfomationDto update(ItUnitInfomationDto itUnitInfomationDto, long id,long orgId) throws Exception {
		ItUnitInfomationEntity itUnitInfomationEntityUpdating = bindDtoToEntity(itUnitInfomationDto, orgId);
		ItUnitInfomationDto itUnitInfomationDtoReturn = new ItUnitInfomationDto();
		if (isPresentExistingOne(id)) {
			itUnitInfomationEntityUpdating = itUnitInfoRepository.save(itUnitInfomationEntityUpdating);
			itUnitInfomationDtoReturn = bindEntityToDto(itUnitInfomationEntityUpdating);
		}
		return itUnitInfomationDtoReturn;
	}

	@Override
	public void delete(long id) throws Exception {
		Optional<ItTeamClassificationEntity> classificationEntity = classificationRepository.findById(id);
		if (classificationEntity.isPresent()) {
			classificationRepository.deleteById(id);
		}
	}

	/*@Override
	public void addTeamClassification(ItUnitInfomationDto itUnitInfomationDto) {
		itUnitInfomationDto.getItTeamClassificationList().add(new ItTeamClassificationDto());	
	}*/

	/*@Override
	public void removeTeamClassification(ItUnitInfomationDto itUnitInfomationDto, Integer index) {
		itUnitInfomationDto.getItTeamClassificationList().remove(index.intValue());
	}
*/
	@Override
	public ItTeamClassificationDto getItTeamClassificatioObj() throws Exception{
		return new ItTeamClassificationDto();
	}

	@Transactional
	@Override
	public boolean saveMasterForm(ItUnitInfomationDto itUnitInfomationDto,long orgId) throws Exception {
		if(isValid(itUnitInfomationDto)) {
			throw new RuntimeException("Validation failed"); 
		}
		
		ItUnitInfomationEntity entity = new ItUnitInfomationEntity();

		List<ItTeamClassificationEntity> itTeamClassificationEntity = new ArrayList<>();
		
		if (itUnitInfomationDto.getId() != 0) {
			entity.setId(itUnitInfomationDto.getId());
			Optional<ItUnitInfomationEntity> savedObject = itUnitInfoRepository.findById(itUnitInfomationDto.getId());
			if(savedObject.isPresent()) {
				itTeamClassificationEntity = savedObject.get().getItTeamClassificationList();
			}
		}
		entity.setNameOfDirector(itUnitInfomationDto.getNameOfDirector());
		entity.setTitle(itUnitInfomationDto.getTitle());
		entity.setAddress(itUnitInfomationDto.getAddress());
		entity.setEmail(itUnitInfomationDto.getEmail());
		entity.setTelNo(itUnitInfomationDto.getTelNo());
		entity.setStaffStrength(itUnitInfomationDto.getStaffStrength());

		
		if (!itUnitInfomationDto.getItTeamClassificationList().isEmpty()) {
			for (ItTeamClassificationDto item : itUnitInfomationDto.getItTeamClassificationList()) {
				ItTeamClassificationEntity classificationEntity = new ItTeamClassificationEntity();
				BeanUtils.copyProperties(item, classificationEntity);
				itTeamClassificationEntity.add(classificationEntity);
			}
		}

		entity.setItTeamClassificationList(itTeamClassificationEntity);

		//User user = getLoggedUserDetails(principal);
		ManageInstituteEntity manageInstituteEntity = new ManageInstituteEntity();
		manageInstituteEntity.setId(orgId);
		entity.setInstitution(manageInstituteEntity);

		itUnitInfoRepository.save(entity);
		return true;
	}
	
	private boolean isValid(ItUnitInfomationDto itUnitInfomationDto) {
		boolean valid = true;
		if(Pattern.matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$", itUnitInfomationDto.getTelNo())) {
			return false;
		}
		
		return valid;
	}

	@Transactional
	@Override
	public void saveChildForm(ItTeamClassificationDto classificationDto,long orgId) throws Exception {
		ItTeamClassificationEntity classificationEntity = new ItTeamClassificationEntity();
		BeanUtils.copyProperties(classificationDto, classificationEntity);
		if (classificationDto.getId() != 0) {
			classificationEntity.setId(classificationDto.getId());
		}

		//User user = getLoggedUserDetails(principal);
		ManageInstituteEntity manageInstituteEntity = new ManageInstituteEntity();
		manageInstituteEntity.setId(orgId);

		Optional<ItUnitInfomationEntity> itUnitInfoEntity = itUnitInfoRepository.findByInstitution(manageInstituteEntity);

		if (itUnitInfoEntity.isPresent()) {
			List<ItTeamClassificationEntity> classificationEntities = itUnitInfoEntity.get().getItTeamClassificationList();
			classificationEntities.add((classificationEntity));
			itUnitInfoRepository.save(itUnitInfoEntity.get());
		} else {

		}

	}

	@Override
	public ItTeamClassificationDto requestChildFormData(long id) throws Exception{
		Optional<ItTeamClassificationEntity> classificationEntity = classificationRepository.findById(id);

		if (classificationEntity.isPresent()) {
			ItTeamClassificationDto classificationDto = new ItTeamClassificationDto();
			BeanUtils.copyProperties(classificationEntity.get(), classificationDto);
			return classificationDto;
		}

		return null;
	}

	@Override
	public void updateForm(long id, ItTeamClassificationDto classificationDto) throws Exception {
		Optional<ItTeamClassificationEntity> classificationEntity = classificationRepository.findById(id);

		if (classificationEntity.isPresent()) {
			ItTeamClassificationEntity entity = new ItTeamClassificationEntity();
			BeanUtils.copyProperties(classificationDto, entity);
			entity.setId(id);
			classificationRepository.save(entity);
		}
	} 

}
