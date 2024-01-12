package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Web.dto.ItTeamClassificationDto;
import com.ictasl.java.projectinventory.Web.dto.ItUnitInfomationDto;

public interface ItUnitInfomationService {

	ItUnitInfomationDto getItUnitInforInOrg(long orgId) throws Exception;

	ItUnitInfomationDto update(ItUnitInfomationDto itUnitInfomationDto, long id,long orgId) throws Exception;

	void delete(long id) throws Exception;

	//void addTeamClassification(ItUnitInfomationDto itUnitInfomationDto);

	//void removeTeamClassification(ItUnitInfomationDto itUnitInfomationDto, Integer index);

	ItTeamClassificationDto getItTeamClassificatioObj() throws Exception;

	boolean saveMasterForm(ItUnitInfomationDto itUnitInfomationDto, long orgId) throws Exception;

	void saveChildForm(ItTeamClassificationDto classificationDto, long orgId) throws Exception;

	ItTeamClassificationDto requestChildFormData(long id) throws Exception;

	void updateForm(long id, ItTeamClassificationDto classificationDto) throws Exception;

}
