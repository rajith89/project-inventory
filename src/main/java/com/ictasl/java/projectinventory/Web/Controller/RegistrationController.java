package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.InstituteManageService;
import com.ictasl.java.projectinventory.Service.UserService;
import com.ictasl.java.projectinventory.Web.dto.InstituteManageDto;
import com.ictasl.java.projectinventory.Web.dto.Pager;
import com.ictasl.java.projectinventory.Web.dto.UserDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import com.ictasl.java.projectinventory.captcha.ICaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class RegistrationController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int[] PAGE_SIZES = {5, 10, 20};

    @Value("${page.size}")
    private int initialPageSize;

    @Autowired
    private UserService userService;

    @Autowired
    private InstituteManageService instituteManageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ICaptchaService captchaService;

    @RequestMapping(value = "/registrations", method = RequestMethod.GET)
    public ModelAndView registration() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        UserDto user = new UserDto();
        modelAndView.addObject("userDto", user);
        modelAndView.addObject("isAdmin", isAdmin());
        modelAndView.addObject("selectObj", instituteManageService.getInsNameList());
        modelAndView.setViewName("registrations");
        return modelAndView;
    }


    @RequestMapping(value = "/registrations", method = RequestMethod.POST)
    public String createNewUser(@Valid final UserDto accountDto, RedirectAttributes redirAttrs) {

        // final String response = request.getParameter("g-recaptcha-response");
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(accountDto.getUserName());
        if (userExists != null) {
            redirAttrs.addFlashAttribute("message", "User Name Already Exist!");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        } else {
            try {
                InstituteManageDto institute = instituteManageService.getInstitution(accountDto.getInsId());
                accountDto.setInsName(institute.getName());

                userService.createUserAccount(accountDto);
                redirAttrs.addFlashAttribute("message", "User has been registered successfully");
                redirAttrs.addFlashAttribute("alertClass", "alert-success");
                modelAndView.addObject("user", new User());
                modelAndView.setViewName("successRegister");
            } catch (Exception e) {
                redirAttrs.addFlashAttribute("message", "Error occurred When Creating User");
                redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            }
        }

        return "redirect:/user/list";
    }

    @GetMapping("/list")
    public String existingUserList(Model model) {
        model.addAttribute("Institute", "test ins");
       return this.findPaginated(Optional.of(initialPageSize),Optional.of(1), model);
    }

    @GetMapping("/edit/{id}")
    public String showUsers(@PathVariable("id") long id, Model model) {
        try {
            UserDto user = userService.findUserById(id);
            model.addAttribute("existingUser", user);
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return "userUpdate";
    }

    @RequestMapping("/profile")
    public String updateProfile(Model model, HttpSession session) {
        try {
            UserDto user = userService.findUserById(getUserId(session));
            model.addAttribute("existingUser", user);
            model.addAttribute("isAdmin", isAdmin());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return "profileupdate";
    }

    @PostMapping("edit/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid UserDto user, RedirectAttributes redirAttrs, BindingResult result,
                             Model model) {
        User userExists = userService.findUserByUserName(user.getUserName());
//        if (userExists != null) {
//            redirAttrs.addFlashAttribute("message", "User Name Already Exist!");
//            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
//        }
        try {
            String firstName = userExists.getFirstName()!=null?userExists.getFirstName():"";
            String lastName = userExists.getLastName()!=null?userExists.getLastName():"";
            long insId = userExists.getInsId();

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setInsId(insId);
            user.setId(userExists.getId());

            userService.updateUser(id, user);
            redirAttrs.addFlashAttribute("successMessage", "User has been updated successfully");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (ResourceNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            model.addAttribute("users", userService.getAllUsers(1));
            model.addAttribute("isAdmin", isAdmin());
        }
        return "redirect:/user/list";
    }

    @PostMapping("update/{id}")
    public String updateProfile(@PathVariable("id") long id, @Valid UserDto user, RedirectAttributes redirAttrs, BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "userlist";
        }
        try {
            userService.updateUser(id, user);
            redirAttrs.addFlashAttribute("message", "User Details Updated Successfully!");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (ResourceNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            model.addAttribute("users", userService.getAllUsers(1));
            model.addAttribute("isAdmin", isAdmin());
        }
        return "redirect:/user/profile";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) throws Exception {
        userService.deleteUser(id);
        model.addAttribute("users", userService.getAllUsers(1));
        model.addAttribute("isAdmin", isAdmin());
        return "redirect:/user/list";
    }

    private long getUserId(HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userRepository.findByUserName(username);

        }

        return user.getId();
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


    @GetMapping("/list/")
    public String findPaginated(@RequestParam("pageSize") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page, Model model){
        int evalPageSize = pageSize.orElse(initialPageSize);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        Page<User> users = userService.findPaginated(evalPage, evalPageSize);
        Pager pager = new Pager(users.getTotalPages(), users.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("users", users);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pager", pager);
        model.addAttribute("isAdmin", isAdmin());
        return "userlist";
    }

}
