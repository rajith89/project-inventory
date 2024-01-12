package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.NewProjectRepository;
import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.NewProject;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.SubProcurement;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.NewProjectService;
import com.ictasl.java.projectinventory.Service.SubProcurementService;
import com.ictasl.java.projectinventory.Web.dto.ReviewRequestDto;
import com.ictasl.java.projectinventory.Web.dto.SubProcurementDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/newproject")
public class NewProjectController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Autowired
    private NewProjectService newProjectService;

    @Autowired
    private SubProcurementService subProcurementService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewProjectRepository newProjectRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newProject", newProjectService.createNewProject());
        model.addAttribute("isAdmin", isAdmin());
        return "newProject";
    }

    @PostMapping("/")
    public String saveProject(@Valid NewProject newProject, BindingResult bindingResult, RedirectAttributes redirAttrs, Model model) {
        int i = 1;
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "The submitted data has errors.");
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        } else {

            try {
                for (SubProcurement subProcurement : newProject.getSubProcurements()) {
                    subProcurement.setSequenceNumber(i);
                    i++;
                }
                model.addAttribute("newProject", newProjectService.saveNewProject(newProject));
                model.addAttribute("successMessage", "The record has been added successfully.");
                redirAttrs.addFlashAttribute("message", "The record has been added successfully.");
                redirAttrs.addFlashAttribute("alertClass", "alert-success");
                model.addAttribute("isAdmin", isAdmin());
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                redirAttrs.addFlashAttribute("error", "Error Saving...");
                redirAttrs.addFlashAttribute("alertClass", "alert-danger");
                model.addAttribute("isAdmin", isAdmin());
            }
        }
        return "redirect:/newproject/list";
    }

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String listProjects(ModelMap model, RedirectAttributes redirAttrs, HttpSession session) {
        try {
            List<NewProject> newProjects = newProjectService.findAllNewProjectByInstutionId(getInsId(session));
            model.addAttribute("newProjects", newProjects);
            model.addAttribute("metaTitle", "All New Projects");
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error occurred when fetching the New Projects");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }

        return "newProjectList";
    }


    @GetMapping("/edit/{id}")
    public String getForm(Model model, @PathVariable(required = false, name = "id") Long id, RedirectAttributes redirAttrs) {
        try {
            model.addAttribute("newproj", newProjectRepository.findById(id));
            SubProcurementDto subProcurementDto = new SubProcurementDto();
            subProcurementDto.setSubProcurements(subProcurementService.getSubProcurementByNewProjectId(id));
            model.addAttribute("subProcurementDto", subProcurementDto);
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception ex) {
            redirAttrs.addFlashAttribute("message", "Error occurred when fetching the New Project");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            LOGGER.error(ex.getMessage());
            model.addAttribute("isAdmin", isAdmin());
        }

        return "newProjectEdit";
    }

    @PostMapping("/addsubprocrement")
    public String addSubProcurement(NewProject existingProject, RedirectAttributes redirAttrs) {
        try {
            newProjectService.addSubProcurement(existingProject);

        } catch (Exception ex) {
            redirAttrs.addFlashAttribute("message", "Error occurred adding Sub Procurement");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            LOGGER.error(ex.getMessage());
        }
        return "newProject :: subProcurementsList"; // returning the updated section
    }

    @PostMapping("/removesubprocrement")
    public String removeSubProcurement(NewProject newProject, @RequestParam("removeDynamicRow") Integer index, RedirectAttributes redirAttrs) {
        try {
            newProjectService.removeSubProcurement(newProject, index);

        } catch (Exception ex) {
            redirAttrs.addFlashAttribute("message", "Error occurred removing Sub Procurement");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            LOGGER.error(ex.getMessage());
        }
        return "newProject :: subProcurementsList"; // returning the updated section
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String removeSubProcurement(Model model, @PathVariable(required = true, name = "id") Long id, RedirectAttributes redirAttrs) {
        try {
            newProjectRepository.deleteById(id);
            redirAttrs.addFlashAttribute("message", "The record has been deleted successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error Deleting New Project .");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }

        return "redirect:/newproject/list";
    }

    @PostMapping("/subprocurement/review")
    public ResponseEntity<Object> reviewExistingSolution(@Valid ReviewRequestDto reviewExisting, BindingResult result, RedirectAttributes redirAttrs, Model model) {
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            //return "redirect:/existingSolution";
        }
        SubProcurement dto = new SubProcurement();
        try{
            dto =subProcurementService.reviewSubProcurement(reviewExisting);

            redirAttrs.addFlashAttribute("message", "The record has been added successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        }catch (Exception ex){
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return new ResponseEntity<>(dto,new HttpHeaders(),HttpStatus.OK);
    }


    private long getInsId(HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userRepository.findByUserName(username);

        }

        boolean isAdmin = false;
        for (Role role : user.getRoles()) {
            isAdmin = role.getRole().equals("ADMIN");
        }

        long adminInsId = 0l;
        long insId = 0l;
        if (session.getAttribute("insid") != null && isAdmin) {
            adminInsId = new Long(session.getAttribute("insid").toString());
        }

        if (adminInsId == 0) {
            insId = user.getInsId();
        } else {
            insId = adminInsId;
        }
        return insId;
    }

    private boolean isAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userRepository.findByUserName(username);

        }

        boolean isAdmin = false;
        for (Role role : user.getRoles()) {
            isAdmin = role.getRole().equals("ADMIN");
        }
        return isAdmin;
    }

}
