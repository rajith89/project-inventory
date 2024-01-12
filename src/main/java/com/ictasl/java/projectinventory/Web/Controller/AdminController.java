package com.ictasl.java.projectinventory.Web.Controller;


import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.InstituteManageService;
import com.ictasl.java.projectinventory.Service.MinistryService;
import com.ictasl.java.projectinventory.Service.UserService;
import com.ictasl.java.projectinventory.Web.dto.AdminAccessDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private InstituteManageService instituteService;

    @Autowired
    private MinistryService ministryService;

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public ModelAndView adminHome(Model model) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);

        User user = userService.findUserByUserName(auth.getName());

        boolean isAdmin = false;
        for (Role role : user.getRoles()) {
            isAdmin = role.getRole().equals("ADMIN");
        }

        AdminAccessDTO adminAccessDTO = new AdminAccessDTO();

        modelAndView.addObject("adminAccessDTO", adminAccessDTO);

        modelAndView.addObject("minList", ministryService.getMinistryNameList());

        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";

        modelAndView.addObject("userName", "Welcome " + firstName + " " + lastName + " (" + user.getUserName() + ")");
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin");
        model.addAttribute("isAdmin", isAdmin);
        return modelAndView;
    }

    @RequestMapping(value = "userhome", method = RequestMethod.GET)
    public ModelAndView userHome(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);

        User user = userService.findUserByUserName(auth.getName());

        boolean isAdmin = false;
        for (Role role : user.getRoles()) {
            isAdmin = role.getRole().equals("ADMIN");
        }

        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";

        modelAndView.addObject("userName", "Welcome " + firstName + " " + lastName + " (" + user.getUserName() + ")");
        modelAndView.addObject("adminMessage", "Content Available Only for Users with User Role");
        modelAndView.setViewName("user");
        model.addAttribute("isAdmin", isAdmin);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/{insId}", method = RequestMethod.GET)
    public @ResponseBody
    List<HashMap<String, String>> setInsId(@PathVariable("insId") int insId, HttpSession session) throws Exception {
        session.setAttribute("insid", insId);
        return null;
    }

    @RequestMapping(value = "/institution/{ministryId}", method = RequestMethod.GET)
    public @ResponseBody
    List<HashMap<String, String>> getAllSubcategories(@PathVariable("ministryId") int ministryId) throws Exception {
        return instituteService.getAllInsByMinId(ministryId);
    }

}
