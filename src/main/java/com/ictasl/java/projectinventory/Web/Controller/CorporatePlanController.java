package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.ProjectFileUploadService;
import com.ictasl.java.projectinventory.Web.dto.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class CorporatePlanController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProjectFileUploadService projectFileUploadService;

	@Autowired
	private UserRepository userRepository;
	
	@Value("${project.file.max.size}")
	private long maxSize;

	@RequestMapping(value = "/projectPlan", method = RequestMethod.GET)
	public ModelAndView fileUpload(Principal principal, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("uploadFiles");
		model.addAttribute("isAdmin", isAdmin());
		return modelAndView;
	}
	

	@PostMapping("/projectPlans")
	public String uploadProjectPlans(@RequestParam("files") MultipartFile[] files, RedirectAttributes redirAttrs,HttpSession session) {
		ResponseMessage res = new ResponseMessage();
		try {
			if (files.length > 0) {
				res = projectFileUploadService.uploadFiles(files, getInsId(session));
			} else {
				redirAttrs.addFlashAttribute("message", "Please attach a file.");
				redirAttrs.addFlashAttribute("alertClass", "alert-danger");
				//res.setMessage("Please select at least 1 file.");
			}
			redirAttrs.addFlashAttribute("message", "The file has been added successfully.");
			redirAttrs.addFlashAttribute("alertClass", "alert-success");
			//redirAttrs.addFlashAttribute("success", res);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//redirAttrs.addFlashAttribute("message", "The record has been added failed");
			//redirAttrs.addFlashAttribute("alertClass", "alert-danger");
			return "redirect:/projectPlans";
		}
		return "redirect:/projectPlans/list";
	}

	@GetMapping("/projectPlans/list")
	public ModelAndView getUserRelatedProjectPlans(Model model, HttpServletRequest request,HttpSession session) {
		try {
			List<HashMap<String, Object>> map = new ArrayList<HashMap<String,Object>>();
			map = projectFileUploadService.getUserRelatedFiles(request , getInsId(session));				
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("docs", map);
			modelAndView.setViewName("showFiles");
			model.addAttribute("docs", map);
			model.addAttribute("isAdmin", isAdmin());

			return modelAndView;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	@GetMapping("/download/{id}")
	ResponseEntity<Resource> downLoadSingleFile(@PathVariable long id, HttpServletRequest request) {
		String mimeType = null;
		Resource resource = null;
		try {
			resource = projectFileUploadService.downloadFile(id);

			try {
				mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException e) {
				mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
				LOGGER.error(e.getMessage());
			}
			mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
				.header(HttpHeaders.CONTENT_DISPOSITION, /*"inline;fileName=" +*/"attachment; filename=\""+ resource.getFilename()).body(resource);
	}

	@GetMapping("/delete/{id}")
	public String deleteUserRelatedProjectPlans(@PathVariable long id , RedirectAttributes redirAttrs, Model model) {
		try {
			ResponseMessage res = projectFileUploadService.deleteFile(id);
			redirAttrs.addFlashAttribute("message", "The record has been deleted successfully.");
			redirAttrs.addFlashAttribute("alertClass", "alert-success");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return "redirect:/projectPlans/list";
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file;
		try {
			file = projectFileUploadService.load(filename);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return null;

	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(maxSize);
		return multipartResolver;
	}

	private long getInsId(HttpSession session){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = null;
		if (principal instanceof UserDetails) {
			String username = ((UserDetails)principal).getUsername();
			user = userRepository.findByUserName(username);

		}

		boolean isAdmin = false;
		for(Role role:user.getRoles()) {
			isAdmin = role.getRole().equals("ADMIN");
		}

		long adminInsId = 0l;
		long insId = 0l;
		if(session.getAttribute("insid")!=null && isAdmin){
			adminInsId = new Long(session.getAttribute("insid").toString());
		}

		if(adminInsId==0){
			insId = user.getInsId();
		}else{
			insId=adminInsId;
		}
		return insId;
	}

	private boolean isAdmin(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = null;
		if (principal instanceof UserDetails) {
			String username = ((UserDetails)principal).getUsername();
			user = userRepository.findByUserName(username);

		}

		boolean isAdmin = false;
		for(Role role:user.getRoles()) {
			isAdmin = role.getRole().equals("ADMIN");
		}
		return isAdmin;
	}
}
