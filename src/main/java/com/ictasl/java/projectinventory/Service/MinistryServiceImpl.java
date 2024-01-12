package com.ictasl.java.projectinventory.Service;

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

import com.ictasl.java.projectinventory.Persistence.dao.MinistryRepository;
import com.ictasl.java.projectinventory.Persistence.entity.MinistryEntity;
import com.ictasl.java.projectinventory.Web.dto.MinistryDto;

@Service
public class MinistryServiceImpl implements MinistryService{

	@Autowired
	private MinistryRepository ministryRepository;
	 
	@Override
	public MinistryDto createMinistry(MinistryDto ministryDto) throws Exception {
		MinistryDto ministryDtoReturn = new MinistryDto();
		if(!isExisting(ministryDto)) {
			MinistryEntity ministryEntity = new MinistryEntity();
			BeanUtils.copyProperties(ministryDto, ministryEntity);
			ministryEntity = ministryRepository.save(ministryEntity);
			BeanUtils.copyProperties(ministryEntity, ministryDtoReturn);			
		}
		
		return ministryDtoReturn;
	}
	
	private boolean isExisting(MinistryDto ministryDto) throws Exception{
		Optional<MinistryEntity> ministryEntityExisting = ministryRepository.findByName(ministryDto.getName());
		if(ministryEntityExisting.isPresent()) {
			throw new RuntimeException("Ministry Name Already Existing.");
		}
		return false;
	}
	
	@Override
	public  List<MinistryDto> getAllSavedList()  throws Exception{
		
		List<MinistryEntity> ministryList = (List<MinistryEntity>) ministryRepository.findAll();
		List<MinistryDto> ministryDtoReturnList = new ArrayList<>();
		if(!ministryList.isEmpty()) {
			ministryDtoReturnList = ministryList.stream().map(p -> {
				MinistryDto ministryDtoReturn = new MinistryDto();
				BeanUtils.copyProperties(p, ministryDtoReturn);
				return ministryDtoReturn;
			}).collect(Collectors.toList());
		}
		return ministryDtoReturnList;
	}

	@Override
	public MinistryDto editMinistry(long id) throws Exception{
		MinistryDto returnMinistryDto = new MinistryDto();
		Optional<MinistryEntity> existingMinistryEntity = ministryRepository.findById(id);
		if (existingMinistryEntity.isPresent()) {
			BeanUtils.copyProperties(existingMinistryEntity.get(), returnMinistryDto);
		}
		return returnMinistryDto;
	}

	@Override
	public MinistryDto update(long id, MinistryDto ministry) throws Exception {
		MinistryDto returnMinistryDto = new MinistryDto();
		Optional<MinistryEntity> existingMinistryEntity = ministryRepository.findById(id);
		if (existingMinistryEntity.isPresent()) {
			Optional<MinistryEntity> existingMinistryByName = ministryRepository.findByName(ministry.getName());
			if(existingMinistryByName.isPresent() && existingMinistryByName.get().getId() != id) {
				throw new RuntimeException("Name Already Existing");
			}
			MinistryEntity ministryEntity = new MinistryEntity();
			BeanUtils.copyProperties(ministry, ministryEntity);
			ministryEntity = ministryRepository.save(ministryEntity);
			BeanUtils.copyProperties(ministryEntity, returnMinistryDto);
		}
		return returnMinistryDto;
	}

	@Override
	public void delete(long id) throws Exception{
		Optional<MinistryEntity> existingMinistryEntity = ministryRepository.findById(id);
		if (existingMinistryEntity.isPresent()) {
			ministryRepository.deleteById(id);
		}
	}

	@Override
	public List<HashMap<String, String>> getMinistryNameList(){
		List<MinistryEntity> ministryList = (List<MinistryEntity>) ministryRepository.findAll();

		List<HashMap<String, String>> returnList = ministryList.stream().map(m->{
			HashMap<String,String> map = new HashMap<>();
			map.put("name", m.getName());
			map.put("id", Long.toString(m.getId()));

			return map;
		}).collect(Collectors.toList());
		return returnList;
	}

    @Override
    public Page<MinistryEntity> findPaginated(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo,pageSize);
		return this.ministryRepository.findAll(pageable);
    }
}
