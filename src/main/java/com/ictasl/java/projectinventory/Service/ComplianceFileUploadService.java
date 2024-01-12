package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Web.dto.ResponseMessage;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

public interface ComplianceFileUploadService {
    ResponseMessage uploadFiles(MultipartFile[] files, long orgId) throws Exception;

    List<HashMap<String, Object>> getUserRelatedFiles(HttpServletRequest request, long orgId) throws Exception;

    Resource load(String filename) throws Exception;

    List<HashMap<String, Object>> loadAllFiles(HttpServletRequest request)throws Exception;

    ResponseMessage deleteFile(long id) throws Exception;

    Resource downloadFile(long id) throws Exception;

    List<HashMap<String, Object>> getFilesListFormAdmin(HttpServletRequest request) throws Exception;
}
