package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.GovernmentServicesRepository;
import com.ictasl.java.projectinventory.Persistence.dao.InstituteManagementRepository;
import com.ictasl.java.projectinventory.Persistence.entity.*;
import com.ictasl.java.projectinventory.Web.dto.GovernmentServiceConnTypeDTO;
import com.ictasl.java.projectinventory.Web.dto.GovernmentServicesDTO;
import com.ictasl.java.projectinventory.Web.dto.InstituteManageDto;
import com.ictasl.java.projectinventory.Web.dto.InstitutionDetailsDTO;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GovernmentServiceImpl implements GovernmentService{


	@Autowired
	private InstituteManagementRepository instituteManagementRepository;

	@Autowired
	private GovernmentServicesRepository gsRepo;


	@Override
	public void createServiceDetails(GovernmentServicesDTO governmentServicesDTO){
		GovernmentServicesEntity governmentServicesEntity = new GovernmentServicesEntity();
		BeanUtils.copyProperties(governmentServicesDTO, governmentServicesEntity);

		ManageInstituteEntity institute = new ManageInstituteEntity();
		institute.setId(governmentServicesDTO.getInsId());

		governmentServicesEntity.setInstitute(institute);

		GovernmentServiceConnectivityTypes connectivityTypes = new GovernmentServiceConnectivityTypes();
		if(governmentServicesDTO.getConnectivityType().getLGN()!=null){
			connectivityTypes.setLgn(governmentServicesDTO.getConnectivityType().getLGN());
		}
		if(governmentServicesDTO.getConnectivityType().getADSL()!=null){
			connectivityTypes.setAdsl(governmentServicesDTO.getConnectivityType().getADSL());
		}
		if(governmentServicesDTO.getConnectivityType().getDongle()!=null){
			connectivityTypes.setDongle(governmentServicesDTO.getConnectivityType().getDongle());
		}
		governmentServicesEntity.setConnectionTypes(connectivityTypes);

		gsRepo.save(governmentServicesEntity);

	}

	@Override
	public GovernmentServicesDTO editGovernmentService(long id) {

		GovernmentServicesDTO governmentServicesDTO = new GovernmentServicesDTO();

		Optional<GovernmentServicesEntity> governmentServicesEntity = gsRepo.findById(id);
		if (governmentServicesEntity.isPresent()) {
			BeanUtils.copyProperties(governmentServicesEntity.get(), governmentServicesDTO);
			if(governmentServicesEntity.get().getInstitute() != null) {
				governmentServicesDTO.setInsId(governmentServicesEntity.get().getInstitute().getId());
				governmentServicesDTO.setInsName(governmentServicesEntity.get().getInstitute().getName());
			}
		}else{
			return new GovernmentServicesDTO();
		}
		return governmentServicesDTO;
	}

	@Override
	public void updateGovernmentService(long id,GovernmentServicesDTO governmentServicesDTO) {
		GovernmentServicesDTO governmentServicesDTOSaved = new GovernmentServicesDTO();
		GovernmentServicesEntity governmentServicesEntity = new GovernmentServicesEntity();
		Optional<GovernmentServicesEntity> governmentServicesOptional = gsRepo.findById(id);
		if (governmentServicesOptional.isPresent()) {
			BeanUtils.copyProperties(governmentServicesDTO, governmentServicesEntity);
			ManageInstituteEntity instituteEntity = new ManageInstituteEntity();
//			instituteEntity.setName("test");
			instituteEntity.setId(governmentServicesDTO.getInsId());
			governmentServicesEntity.setInstitute(instituteEntity);

			GovernmentServiceConnectivityTypes connectivityTypes = new GovernmentServiceConnectivityTypes();
			if(governmentServicesDTO.getConnectivityType().getLGN()!=null){
				connectivityTypes.setLgn(governmentServicesDTO.getConnectivityType().getLGN());
			}
			if(governmentServicesDTO.getConnectivityType().getADSL()!=null){
				connectivityTypes.setAdsl(governmentServicesDTO.getConnectivityType().getADSL());
			}
			if(governmentServicesDTO.getConnectivityType().getDongle()!=null){
				connectivityTypes.setDongle(governmentServicesDTO.getConnectivityType().getDongle());
			}
			governmentServicesEntity.setConnectionTypes(connectivityTypes);

			gsRepo.save(governmentServicesEntity);
			
		}
	}

	@Override
	public List<GovernmentServicesDTO> getAllServicesList() {
		List<GovernmentServicesEntity> governmentServicesEntityList = (List<GovernmentServicesEntity>) gsRepo.findAll();
		List<GovernmentServicesDTO> governmentServicesReturnList = new ArrayList<>();
		if(!governmentServicesEntityList.isEmpty()) {
			governmentServicesReturnList = governmentServicesEntityList.stream().map(p -> {
				GovernmentServicesDTO governmentServiceDtoReturn = new GovernmentServicesDTO();
				BeanUtils.copyProperties(p, governmentServiceDtoReturn);
				if(p.getInstitute() != null) {
					governmentServiceDtoReturn.setInsId(p.getInstitute().getId());
					governmentServiceDtoReturn.setInsName(p.getInstitute().getName());
				}
				return governmentServiceDtoReturn;
			}).collect(Collectors.toList());
		}
		System.out.println(governmentServicesReturnList.size());
		return governmentServicesReturnList;
	}

	@Override
	public void deleteGovernmentService(long id) {
		Optional<GovernmentServicesEntity> governmentServicesEntity = gsRepo.findById(id);
		if (governmentServicesEntity.isPresent()) {
			gsRepo.deleteById(id);
		}
	}

	@Override
	public GovernmentServicesDTO getServices(Long id) {
		Optional<GovernmentServicesEntity> governmentServicesEntityOptional = gsRepo.findByInstituteId(id);
		if (!governmentServicesEntityOptional.isPresent()) {
			GovernmentServicesDTO governmentServicesDTO = new GovernmentServicesDTO();

			GovernmentServiceConnTypeDTO connTypeDTO = new GovernmentServiceConnTypeDTO();
			connTypeDTO.setDongle("");
			connTypeDTO.setADSL("");
			connTypeDTO.setLGN("");

			governmentServicesDTO.setConnectivityType(connTypeDTO);
			governmentServicesDTO.setInsId(id);
			return governmentServicesDTO;
		}
		GovernmentServicesEntity governmentServicesEntity = governmentServicesEntityOptional.get();
		GovernmentServicesDTO governmentServicesDTO = new GovernmentServicesDTO();
		BeanUtils.copyProperties(governmentServicesEntity,governmentServicesDTO);

		governmentServicesDTO.setInsId(governmentServicesEntity.getInstitute().getId());
		governmentServicesDTO.setInsName(governmentServicesEntity.getInstitute().getName());

		GovernmentServiceConnTypeDTO connTypeDTO = new GovernmentServiceConnTypeDTO();

		String adsl = governmentServicesEntity.getConnectionTypes().getAdsl()!=null?governmentServicesEntity.getConnectionTypes().getAdsl():"";
		connTypeDTO.setADSL(adsl);

		String lgn = governmentServicesEntity.getConnectionTypes().getLgn()!=null?governmentServicesEntity.getConnectionTypes().getLgn():"";
		connTypeDTO.setLGN(lgn);

		String dongle = governmentServicesEntity.getConnectionTypes().getDongle()!=null?governmentServicesEntity.getConnectionTypes().getDongle():"";
		connTypeDTO.setDongle(dongle);

		governmentServicesDTO.setConnectivityType(connTypeDTO);
		return governmentServicesDTO;
	}

	@Override
	public boolean isDetailsAvailable(Long id) {
		Optional<GovernmentServicesEntity> governmentServicesEntity = gsRepo.findById(id);
		if (governmentServicesEntity.isPresent()) {
			return true;
		}
		return false;
	}

	}

