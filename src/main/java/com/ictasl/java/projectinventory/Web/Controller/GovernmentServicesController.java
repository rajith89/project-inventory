package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.GovernmentService;
import com.ictasl.java.projectinventory.Service.InstituteManageService;
import com.ictasl.java.projectinventory.Web.dto.GovernmentServicesDTO;
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
import java.security.Principal;

@Controller
@RequestMapping("/governmentservices")
public class GovernmentServicesController {

    @Autowired
    private GovernmentService governmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstituteManageService instituteManageService;

    @GetMapping("/")
    public ModelAndView governmentSolutionForm() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        GovernmentServicesDTO governmentServicesDTO = new GovernmentServicesDTO();
        modelAndView.addObject("selectObj", instituteManageService.getInsNameList());
        modelAndView.addObject("governmentServicesDTO", governmentServicesDTO);
        modelAndView.setViewName("governmentserviceslist");
        return modelAndView;
    }

    @GetMapping("/list")
    public String gsListView(Model model, HttpSession session) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userRepository.findByUserName(username);

        } else {
            String username = principal.toString();
        }

        boolean isAdmin = false;
        for (Role role : user.getRoles()) {
            isAdmin = role.getRole().equals("ADMIN");
        }

        long adminInsId = 0l;
        long insId = 0l;
        if (session.getAttribute("insid") != null) {
            adminInsId = new Long(session.getAttribute("insid").toString());
        }

        if (adminInsId == 0) {
            insId = user.getInsId();
        } else {
            insId = adminInsId;
        }

        GovernmentServicesDTO services = governmentService.getServices(insId);
        model.addAttribute("servicesList", services);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("insId", user.getInsId());
        return "governmentserviceslist";
    }

    @GetMapping("/add")
    public ModelAndView addPageLoad(Model model) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        GovernmentServicesDTO governmentServicesDTO = new GovernmentServicesDTO();
        modelAndView.addObject("add", true);
        modelAndView.addObject("selectObj", instituteManageService.getInsNameList());
        modelAndView.addObject("governmentServicesDTO", governmentServicesDTO);
        modelAndView.setViewName("governmentservicesadd");
        return modelAndView;
    }

    @PostMapping("/add")
    public String addServices(@Valid GovernmentServicesDTO governmentServicesDTO, BindingResult result, RedirectAttributes redirAttrs,
                              Model model) {
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            return "redirect:/governmentservices/";
        }
        try {
            governmentService.createServiceDetails(governmentServicesDTO);
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("message", "Error occurred Updating Government Services Details");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        redirAttrs.addFlashAttribute("alertClass", "alert-success");
        redirAttrs.addFlashAttribute("message", "Government Services Details Updated Successfully");
        return "redirect:/governmentservices/";
    }


    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        try {
            GovernmentServicesDTO governmentServicesDTO = governmentService.getServices(id);
            model.addAttribute("governmentServicesDTO", governmentServicesDTO);
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception ex) {
            return null;
        }
        return "governmentservicesadd";
    }

    @PostMapping("edit/{id}/{ins_id}")
    public String update(@PathVariable("id") long id, @PathVariable("ins_id") long insId, @Valid GovernmentServicesDTO governmentServicesDTO, RedirectAttributes redirAttrs,
                         BindingResult result, Model model, Principal principal) {

        governmentServicesDTO.setInsId(insId);
        if (result.hasErrors()) {
            return "governmentservicesadd";
        }

        if (governmentService.isDetailsAvailable(id)) {
            try {
                governmentService.updateGovernmentService(id, governmentServicesDTO);
            } catch (Exception ex) {
                redirAttrs.addFlashAttribute("message", "Error occurred Updating Government Services Details");
                redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            }
        } else {
            try {
                governmentService.createServiceDetails(governmentServicesDTO);
            } catch (Exception ex) {
                redirAttrs.addFlashAttribute("message", "Error occurred Creating Government Services Details");
                redirAttrs.addFlashAttribute("alertClass", "alert-danger");
                ex.printStackTrace();
            }
        }

        redirAttrs.addFlashAttribute("alertClass", "alert-success");
        redirAttrs.addFlashAttribute("message", "Government Services Details Updated Successfully");
        return "redirect:/governmentservices/list";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        governmentService.deleteGovernmentService(id);
        model.addAttribute("serviceList", governmentService.getAllServicesList());
        return "redirect:/governmentservices/";
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
