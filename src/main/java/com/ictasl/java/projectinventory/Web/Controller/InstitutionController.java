package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.InstituteManageService;
import com.ictasl.java.projectinventory.Service.InstitutionService;
import com.ictasl.java.projectinventory.Web.dto.InstitutionDetailsDTO;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/institution")
public class InstitutionController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private InstituteManageService instituteManageService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ModelAndView institution() {
        ModelAndView modelAndView = new ModelAndView();
        InstitutionDetailsDTO institutionDTO = new InstitutionDetailsDTO();
        modelAndView.addObject("institutionDto", institutionDTO);
        modelAndView.setViewName("institution");
        return modelAndView;
    }

    @GetMapping("/list")
    public String institutionList(Model model, HttpSession session) throws Exception {

        model.addAttribute("institutions", instituteManageService.getInstitution(getInsId(session)));
        model.addAttribute("isAdmin", isAdmin());
        return "institutionlist";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs, HttpSession session) {

        try {
            InstitutionDetailsDTO institutionDto = institutionService.getByInstitution(getInsId(session));
            model.addAttribute("institutionDto", institutionDto);
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        redirAttrs.addFlashAttribute("message", "Error occurred when fetching the created reporting authority/line ministry.");
        redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        return "instititionedit";
    }

    @PostMapping("edit/{id}/{ins_id}")
    public String updateInstitution(@PathVariable("id") long id, @PathVariable("ins_id") long insId, @Valid InstitutionDetailsDTO institutionDTO, RedirectAttributes redirAttrs, BindingResult result,
                                    Model model) {
        institutionDTO.setInstituteId(insId);
        if (result.hasErrors()) {
            return "instititionedit";
        }

        if (institutionService.isDetailsAvailable(id)) {
            try {
                institutionService.updateInstitution(id, institutionDTO);
            } catch (ResourceNotFoundException ex) {
                redirAttrs.addFlashAttribute("message", "Error occurred when Creating institution details");
                LOGGER.error(ex.getMessage());
            }
        } else {
            try {
                institutionService.createInstitution(institutionDTO);
            } catch (Exception ex) {
                redirAttrs.addFlashAttribute("message", "Error occurred when updating institution details");
                LOGGER.error(ex.getMessage());
            }
        }

        redirAttrs.addFlashAttribute("alertClass", "alert-success");
        redirAttrs.addFlashAttribute("message", "Institution Details Updated successfully");

        return "redirect:/institution/list";
    }

    @GetMapping("delete/{id}")
    public String deleteInstitution(@PathVariable("id") long id, Model model) {
        institutionService.deleteInstitution(id);
        model.addAttribute("institutions", institutionService.getAllInstitutions());
        return "redirect:/institution/list";
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
