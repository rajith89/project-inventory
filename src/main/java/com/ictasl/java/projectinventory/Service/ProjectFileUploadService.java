package com.ictasl.java.projectinventory.Service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ictasl.java.projectinventory.Web.dto.ResponseMessage;

public interface ProjectFileUploadService {

	ResponseMessage uploadFiles(MultipartFile[] files,long orgId) throws Exception;

	List<HashMap<String, Object>> getUserRelatedFiles(HttpServletRequest request,long orgId) throws Exception;

	Resource load(String filename) throws Exception;
	
	ResponseMessage deleteFile(long id) throws Exception;

	Resource downloadFile(long id) throws Exception;

	List<HashMap<String, Object>> getFilesListFormAdmin(HttpServletRequest request) throws Exception;

	
}
