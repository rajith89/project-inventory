package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.entity.MinistryEntity;
import com.ictasl.java.projectinventory.Web.dto.MinistryDto;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;

public interface MinistryService {

	MinistryDto createMinistry(MinistryDto ministry) throws Exception;

	 List<MinistryDto> getAllSavedList() throws Exception;

	MinistryDto editMinistry(long id) throws Exception;

	MinistryDto update(long id,MinistryDto ministry) throws Exception;

	void delete(long id) throws Exception;

	List<HashMap<String, String>> getMinistryNameList();

	Page<MinistryEntity> findPaginated(int pageNo, int pageSize);

}
