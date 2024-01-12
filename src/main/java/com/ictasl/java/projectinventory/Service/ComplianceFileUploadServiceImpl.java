package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.ComplianceFileUploadRepository;
import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ComplianceFile;
import com.ictasl.java.projectinventory.Persistence.entity.ProjectFileEntity;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Web.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ComplianceFileUploadServiceImpl implements ComplianceFileUploadService {

	private final static Logger logger = LoggerFactory.getLogger(ComplianceFileUploadService.class);

	@Autowired
	private ComplianceFileUploadRepository complianceFileUploadRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Value("${project.file.saving.path}")
	public String filePath;

	@Value("${project.url}")
	private String url;

	private void createDirectory(String path) throws Exception {
		File theDir = new File(path);
		if (!theDir.exists()) {
			theDir.mkdirs();
			logger.info("Directory created for project file ->>> ", path);
		}
	}
	
	private User getLoggedUserDetails(Principal principal) throws Exception {
		User user = userRepository.findByUserName(principal.getName());
		if(user == null) {
			throw new RuntimeException("No logged user");
		}
		return user;
	}
	
	private String reNameFile(String name) throws Exception{
		int lastDotIndex = name.lastIndexOf('.');
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		Date date = new Date();
		String  datetime = formatter.format(date);

		String s = name.substring(0, lastDotIndex ) +datetime + name.substring(lastDotIndex);
		return s.replaceAll(" ", "_");
	}

	@Transactional
	@Override
	public ResponseMessage uploadFiles(MultipartFile[] files, long orgId) throws Exception {
		ResponseMessage res = new ResponseMessage();
		
		//User user = getLoggedUserDetails(principal);
		long organizationId = orgId;
		//long userId = user.getId();

		String fileStoragePath = filePath + organizationId + "/";// + userId;
		createDirectory(fileStoragePath);

		List<ComplianceFile> projectFileList = complianceFileUploadRepository.findAllByOrgIdAndUserId(organizationId,orgId);
		List<ComplianceFile> fileList = new ArrayList<>();
		List<String> availableFileList = new ArrayList<>();

		for (MultipartFile multipartFile : files) {
			String fileName = reNameFile(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
			String fullFileName = fileStoragePath  + fileName;

			if (!projectFileList.isEmpty()) {
				for (ComplianceFile projectFileEntity : projectFileList) {
					if (fullFileName.equals(projectFileEntity.getPath())) {
						availableFileList.add(fileName);
					}
				}
			}
		}

		if (availableFileList.isEmpty()) {
			for (MultipartFile multipartFile : files) {
				String fileName = reNameFile(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
				String path = fileStoragePath  + fileName;
				Path filePath = Paths.get(path);

				try {
					ComplianceFile entity = new ComplianceFile();
					entity.setName(fileName);
					entity.setOrgId(organizationId);
					//entity.setUserId(userId);
					entity.setPath(path);
					Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
					complianceFileUploadRepository.saveAndFlush(entity);
				} catch (IOException e) {
					throw new RuntimeException("Issue in storing the file", e);
				}
			}
			res.setMessage("Uploaded the files successfully ...!!");
		} else {
			for (String string : availableFileList) {
				StringBuffer bf = new StringBuffer();
				bf.append(string + ",");
				res.setMessage(bf.toString() + " Files are existing ...!!");
			}
		}
		return res;
	}

	@Override
	public List<HashMap<String, Object>> getUserRelatedFiles(HttpServletRequest request,long orgId) throws Exception{
		long organizationId = orgId;//user.getInsId();

		List<ComplianceFile> projectFileList = complianceFileUploadRepository.findAllByOrgId(organizationId);
		List<HashMap<String, Object>> returnFileList = makeFileUrl(projectFileList , request);
		return returnFileList;
	}

	@Override
	public Resource load(String filename) throws Exception {
		return null;
	}
	
	@Override
	public List<HashMap<String, Object>> loadAllFiles(HttpServletRequest request)throws Exception {
		List<ComplianceFile> projectFileList = complianceFileUploadRepository.findAll();
		List<HashMap<String, Object>> returnFileList = makeFileUrl(projectFileList , request);
		return returnFileList;
	}
	
	private List<HashMap<String, Object>> makeFileUrl(List<ComplianceFile> projectFileList, HttpServletRequest request) throws Exception{
		List<HashMap<String, Object>> returnFileList = new ArrayList<>();
		if (!projectFileList.isEmpty()) {
			for (ComplianceFile projectFileEntity : projectFileList) {
				HashMap<String, Object> map = new HashMap<>();
				String fileStoragePath = projectFileEntity.getPath();
				Path path = Paths.get(fileStoragePath);
				Resource resource;
				try {
					resource = new UrlResource(path.toUri());
				} catch (MalformedURLException e) {
					throw new RuntimeException("Issue in reading the file", e);
				}

				if (resource.exists() && resource.isReadable()) {
					String mimeType;
					try {
						mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
					} catch (IOException e) {
						mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
					}
					mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;
					map.put("name", resource.getFilename());
					map.put("resource", resource);
					map.put("id", projectFileEntity.getId());
					returnFileList.add(map);
				} else {
					throw new RuntimeException("the file doesn't exist or not readable");
				}
			}
		}
		return returnFileList;
	}

	@Override
	public ResponseMessage deleteFile(long id) throws Exception {
		ResponseMessage res = new ResponseMessage();
		res.setMessage("delete fail");
		Optional<ComplianceFile> projectFileList = complianceFileUploadRepository.findById(id);
		if (!projectFileList.isPresent()) {
			res.setMessage("No file for delete..!!");
			return res;
		}

		String path = projectFileList.get().getPath();
		File f = new File(path);
		if (f.delete()) {
			complianceFileUploadRepository.deleteById(id);
			logger.info("Delete deleted ->>> ", path);
			res.setMessage("Deleted ...!!");
			return res;

		}
		return res;
	}

	@Override
	public Resource downloadFile(long id) throws Exception {
		Optional<ComplianceFile> projectFileEntity = complianceFileUploadRepository.findById(id);

		if (projectFileEntity.isPresent()) {
			Path path = Paths.get(projectFileEntity.get().getPath());
			Resource resource;
			try {
				resource = new UrlResource(path.toUri());
			} catch (MalformedURLException e) {
				throw new RuntimeException("Issue in reading the file", e);
			}

			if (resource.exists() && resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("the file doesn't exist or not readable");
			}
		}
		return null;
	}

	@Override
	public List<HashMap<String, Object>> getFilesListFormAdmin(HttpServletRequest request) throws Exception {
		List<ComplianceFile> complienceFileList = complianceFileUploadRepository.findAll();
		List<HashMap<String, Object>> returnFileList = makeFileUrl(complienceFileList, request);
		return returnFileList;
	}

}
