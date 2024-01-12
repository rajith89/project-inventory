package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.ExistingProjectRepository;
import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ExistingProject;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.SubProcurement;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.ExistingProjectService;
import com.ictasl.java.projectinventory.Service.ExistingSolutionService;
import com.ictasl.java.projectinventory.Service.SubProcurementService;
import com.ictasl.java.projectinventory.Web.dto.ExistingSolutionDto;
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
@RequestMapping("/existingproject")
public class ExistingProjectController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExistingProjectService existingProjectService;

    @Autowired
    private SubProcurementService subProcurementService;

    @Autowired
    private ExistingProjectRepository existingProjectRepository;

    @Autowired
    private ExistingSolutionService existingSolutionService;


    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("existingProject", existingProjectService.createExistingProject());
        model.addAttribute("existingsolutions", existingSolutionService.getAllExistingSolutions(getInsId(session)));
        model.addAttribute("isAdmin", isAdmin());
        return "existingProject";
    }

    @PostMapping("/")
    public String saveProject(@Valid ExistingProject existingProject, BindingResult bindingResult, RedirectAttributes redirAttrs, Model model) {
        int i = 1;
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "The submitted data has errors.");
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            model.addAttribute("isAdmin", isAdmin());
        } else {

            try {

                for (SubProcurement subProcurement : existingProject.getSubProcurements()) {
                    subProcurement.setSequenceNumber(i);
                    i++;
                }
                model.addAttribute("existingProject", existingProjectService.saveExistingProject(existingProject));
                model.addAttribute("successMessage", "The record has been added successfully.");
                redirAttrs.addFlashAttribute("message", "The record has been added successfully.");
                redirAttrs.addFlashAttribute("alertClass", "alert-success");
                model.addAttribute("isAdmin", isAdmin());

            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                redirAttrs.addFlashAttribute("error", "Error Saving...");
                redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            }

        }
        return "redirect:/existingproject/list";
    }

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String listProjects(ModelMap model, RedirectAttributes redirAttrs, HttpSession session) {
        try {
            List<ExistingProject> existingProjects = existingProjectService.findAllExistingProjectByInstutionId(getInsId(session));
            model.addAttribute("existingsolutions", existingSolutionService.getAllExistingSolutions(getInsId(session)));
            model.addAttribute("existingProjects", existingProjects);
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error occurred when fetching the Exiting Projects");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        //model.addAttribute("metaTitle", "All Existing Projects");
        return "existingProjectList";
    }


    @GetMapping("/edit/{id}")
    public String getForm(Model model, @PathVariable(required = false, name = "id") Long id, RedirectAttributes redirAttrs, HttpSession session) {
        try {
            model.addAttribute("existingproj", existingProjectRepository.findById(id));
            SubProcurementDto subProcurementDto = new SubProcurementDto();
            subProcurementDto.setSubProcurements(subProcurementService.getSubProcurementByExistingProjectId(id));
            model.addAttribute("existingsolutions", existingSolutionService.getAllExistingSolutions(getInsId(session)));
            model.addAttribute("subProcurementDto", subProcurementDto);
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception ex) {
            redirAttrs.addFlashAttribute("message", "Error occurred when fetching the Exisiting Project");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            LOGGER.error(ex.getMessage());
        }
        return "existingProjectEdit";
    }


    @PostMapping("/addsubprocrement")
    public String addContact(ExistingProject existingProject, RedirectAttributes redirAttrs) {
        try {
            existingProjectService.addSubProcurement(existingProject);
        } catch (Exception ex) {
            redirAttrs.addFlashAttribute("message", "Error occurred adding Sub Procurement");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            LOGGER.error(ex.getMessage());
        }
        return "existingProject :: subProcurementsList"; // returning the updated section
    }


    @PostMapping("/removesubprocrement")
    public String removeContact(ExistingProject existingProject, @RequestParam("removeDynamicRow") Integer index, RedirectAttributes redirAttrs) {
        try {
            existingProjectService.removeSubProcurement(existingProject, index);

        } catch (Exception ex) {
            redirAttrs.addFlashAttribute("message", "Error occurred removing Sub Procurement");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            LOGGER.error(ex.getMessage());
        }
        return "existingProject :: subProcurementsList"; // returning the updated section
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String notesDelete(Model model, @PathVariable(required = true, name = "id") Long id, RedirectAttributes redirAttrs) {
        try {
            existingProjectRepository.deleteById(id);
            redirAttrs.addFlashAttribute("message", "The record has been deleted successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error Deleting Existing Project .");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/existingproject/list";
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

    private long getInsId(HttpSession session){
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
