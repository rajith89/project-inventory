package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.InstituteManageService;
import com.ictasl.java.projectinventory.Web.dto.InstituteManageDto;
import com.ictasl.java.projectinventory.Web.dto.Pager;
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

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/createInstitute")
public class InstituteManageController {

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int[] PAGE_SIZES = {5, 10, 20};

	@Value("${page.size}")
	private int initialPageSize;
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private InstituteManageService instituteManageService;

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
    public ModelAndView existingSolutionForm(RedirectAttributes redirAttrs) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            InstituteManageDto instituteManageDto = new InstituteManageDto();
            modelAndView.addObject("selectObj", instituteManageService.getMinistryNameList());
            modelAndView.addObject("institueManageDto", instituteManageDto);
            modelAndView.addObject("isAdmin", isAdmin());
            modelAndView.setViewName("bindInstituteWithMinistry");

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("/list")
    public String instituteListView(Model model, RedirectAttributes redirAttrs) {
        return this.findPaginated(Optional.of(initialPageSize), Optional.of(1), model);
    }

    @GetMapping("/add")
    public ModelAndView addPageLoad() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            InstituteManageDto instituteManageDto = new InstituteManageDto();
            modelAndView.addObject("add", true);
            modelAndView.addObject("selectObj", instituteManageService.getMinistryNameList());
            modelAndView.addObject("isAdmin", isAdmin());
            modelAndView.addObject("institueManageDto", instituteManageDto);
            modelAndView.setViewName("bindInstituteWithMinistry");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return modelAndView;
    }

    @PostMapping("/add")
    public String addInstitute(@Valid InstituteManageDto institueManageDto, BindingResult result, RedirectAttributes redirAttrs,
                               Model model, Principal principal) {
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            return "redirect:/createInstitute/";
        }
        try {
            instituteManageService.createInstitute(institueManageDto, principal);
            redirAttrs.addFlashAttribute("message", "The record has been added successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("message", "The record add failed.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            if (e.getMessage().equals("Institute name already existing")) {
                redirAttrs.addFlashAttribute("message", "Instituion name already exists.");
            }
        }
        return "redirect:/createInstitute/list";
    }


    @GetMapping("/edit/{id}")
    public ModelAndView updateInstitute(@PathVariable("id") long id, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            InstituteManageDto institueManageDto = instituteManageService.editInstitute(id);
            modelAndView.addObject("add", false);
            modelAndView.addObject("selectObj", instituteManageService.getMinistryNameList());
            modelAndView.addObject("institueManageDto", institueManageDto);
            modelAndView.addObject("isAdmin", isAdmin());
            modelAndView.setViewName("bindInstituteWithMinistry");
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") long id, @Valid InstituteManageDto institueManageDto, RedirectAttributes redirAttrs,
                         BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            institueManageDto.setId(id);
            redirAttrs.addFlashAttribute("message", "Failed");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            return "bindInstituteWithMinistryEdit";
        }
        try {
            instituteManageService.update(id, institueManageDto, principal);
            redirAttrs.addFlashAttribute("message", "The record has been updated successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception ex) {
            redirAttrs.addFlashAttribute("message", "The record update failed.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            if (ex.getMessage().equals("Institute name already existing")) {
                redirAttrs.addFlashAttribute("message", "Instituion name already exists.");
            }
            LOGGER.error(ex.getMessage());
        } finally {
            try {
                model.addAttribute("instituteManageDto", instituteManageService.getAllSavedList());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return "redirect:/createInstitute/list";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
        try {
            instituteManageService.delete(id);
            model.addAttribute("savedMinistryList", instituteManageService.getAllSavedList());
            model.addAttribute("isAdmin", isAdmin());
            redirAttrs.addFlashAttribute("message", "The record has been deleted successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("message", "The record can not be deleted.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/createInstitute/list";
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
    public String findPaginated(@RequestParam("pageSize") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page, Model model) {
        int evalPageSize = pageSize.orElse(initialPageSize);
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        Page<ManageInstituteEntity> institues = instituteManageService.findPaginated(evalPage, evalPageSize);
        Pager pager = new Pager(institues.getTotalPages(), institues.getNumber(), BUTTONS_TO_SHOW);


        model.addAttribute("institues", institues);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pager", pager);
        model.addAttribute("isAdmin", isAdmin());
        return "bindInstituteWithMinistryView";
    }

}
