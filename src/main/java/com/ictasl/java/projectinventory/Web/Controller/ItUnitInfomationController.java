package com.ictasl.java.projectinventory.Web.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.ItUnitInfomationService;
import com.ictasl.java.projectinventory.Web.dto.ItTeamClassificationDto;
import com.ictasl.java.projectinventory.Web.dto.ItUnitInfomationDto;

@Controller
@RequestMapping("/itUnit")
public class ItUnitInfomationController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItUnitInfomationService itUnitInfomationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ModelAndView itUnitHome(HttpSession session) {
        ItUnitInfomationDto itUnitInfomationDto = new ItUnitInfomationDto();
        try {
            itUnitInfomationDto = itUnitInfomationService.getItUnitInforInOrg(getInsId(session));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("itUnitDto", itUnitInfomationDto);
        modelAndView.addObject("isDisabled", true);
        modelAndView.addObject("isAdmin", isAdmin());
        if (itUnitInfomationDto.getId() != 0) {
            modelAndView.addObject("isDisabled", false);
        }
        modelAndView.setViewName("itUnitInfoView");
        return modelAndView;
    }

    @GetMapping("/staff")
    public ModelAndView loadItTeamInfoView() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("isAdd", true);
            modelAndView.addObject("itUnitTeamClassification", itUnitInfomationService.getItTeamClassificatioObj());
            modelAndView.addObject("isAdmin", isAdmin());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        modelAndView.setViewName("itTeamForm");
        return modelAndView;
    }

    @PostMapping("/saveMasterForm")
    public String submitTeamClassificationForm(ItUnitInfomationDto itUnitInfomationDto, RedirectAttributes redirAttrs, HttpSession session) {
        try {
            itUnitInfomationService.saveMasterForm(itUnitInfomationDto, getInsId(session));
            redirAttrs.addFlashAttribute("message", "The record has been added successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("error", "Failed");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/itUnit/";
    }

    @PostMapping("/staff/save")
    public String saveChildForm(ItTeamClassificationDto classificationDto, RedirectAttributes redirAttrs, HttpSession session) {
        try {
            itUnitInfomationService.saveChildForm(classificationDto, getInsId(session));
            redirAttrs.addFlashAttribute("message", "The record has been added successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("message", "Failed to save.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return "redirect:/itUnit/";
    }

    @GetMapping("/staff/{id}/edit")
    public ModelAndView editChildFormDataRequest(@PathVariable("id") long id, RedirectAttributes redirAttrs) {
        ItTeamClassificationDto classificationDto;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("itTeamForm");
        try {
            classificationDto = itUnitInfomationService.requestChildFormData(id);
            if (classificationDto != null) {
                modelAndView.addObject("isAdd", false);
                modelAndView.addObject("itUnitTeamClassification", classificationDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("message", "Failed.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return modelAndView;
    }

    @PostMapping("/staff/{id}/edit")
    public String saveEditedChildForm(@PathVariable("id") long id, ItTeamClassificationDto classificationDto, RedirectAttributes redirAttrs) {
        try {
            itUnitInfomationService.updateForm(id, classificationDto);
            redirAttrs.addFlashAttribute("message", "The record has been updated successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("message", "Failed to update.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/itUnit/";
    }

    @PostMapping("/{id}/edit")
    public ModelAndView ChildFormEdit(@PathVariable("id") long id, @Valid ItUnitInfomationDto itUnitInfomationDto, BindingResult result, RedirectAttributes redirAttrs, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("itUnitDto", itUnitInfomationService.update(itUnitInfomationDto, id, getInsId(session)));
            redirAttrs.addFlashAttribute("message", "The record has been updated successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("message", "Failed to update.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return null;
    }

    @GetMapping("/delete/{id}")
    public String itUnitDelete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
        try {
            itUnitInfomationService.delete(id);
            redirAttrs.addFlashAttribute("message", "The record has been deleted successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("message", "Failed to update.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/itUnit/";
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
