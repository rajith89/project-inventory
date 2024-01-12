package com.ictasl.java.projectinventory.Web.Controller;

import java.util.Optional;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.MinistryEntity;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.MinistryService;
import com.ictasl.java.projectinventory.Web.dto.MinistryDto;
import com.ictasl.java.projectinventory.Web.dto.Pager;

@Controller
@RequestMapping("/ministryManagement")
public class MinistryController {

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int[] PAGE_SIZES = {5, 10, 20};

	@Value("${page.size}")
	private int initialPageSize;
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MinistryService ministryService;

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/add")
    public ModelAndView ministryForm() {
        ModelAndView modelAndView = new ModelAndView();
        MinistryDto ministryDto = new MinistryDto();
        modelAndView.addObject("ministryDto", ministryDto);
        modelAndView.setViewName("ministryManagement");
        modelAndView.addObject("isAdmin", isAdmin());
        return modelAndView;
    }

    @GetMapping("/list")
    public String ministryList(Model model, RedirectAttributes redirAttrs) {
		return this.findPaginated(Optional.of(initialPageSize),Optional.of(1), model);
    }

    @PostMapping("/add")
    public String addMinistry(@Valid MinistryDto ministry, BindingResult result, RedirectAttributes redirAttrs, Model model) {
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Error");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/ministryManagement/list";
        }
        try {
            ministryService.createMinistry(ministry);
            redirAttrs.addFlashAttribute("message", "The record has been added successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("error", "The record Already Existing.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            if (e.getMessage().equals("Ministry Name Already Existing.")) {
                redirAttrs.addFlashAttribute("message", "Reporting authority/line Ministry name already exists.");
            }
        }
        model.addAttribute("isAdmin", isAdmin());
        return "redirect:/ministryManagement/list";
    }

    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
        MinistryDto ministryDto = new MinistryDto();
        try {
            ministryDto = ministryService.editMinistry(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("message", "Error occurred when fetching the created reporting authority/line ministry.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        model.addAttribute("ministryDto", ministryDto);
        model.addAttribute("isAdmin", isAdmin());
        return "ministryManagementEdit";
    }

    @PostMapping("edit/{id}")
    public String update(@Valid MinistryDto ministry, @PathVariable("id") long id, RedirectAttributes redirAttrs,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Error");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/ministryManagement/list";
        }
        try {
            ministryService.update(id, ministry);
            redirAttrs.addFlashAttribute("message", "The record has been updated successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error occurred when updating reporting authority/line ministry.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            if (ex.getMessage().equals("Name Already Existing")) {
                redirAttrs.addFlashAttribute("message", "Reporting authority/line ministry name already exists.");
            }
        } finally {
            try {
                model.addAttribute("savedMinistryList", ministryService.getAllSavedList());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                redirAttrs.addFlashAttribute("message", "Error occurred when fetching the created reporting authority/line ministry.");
                redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            }
        }
        model.addAttribute("isAdmin", isAdmin());
        return "redirect:/ministryManagement/list";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
        try {
            ministryService.delete(id);
            model.addAttribute("savedMinistryList", ministryService.getAllSavedList());
            model.addAttribute("isAdmin", isAdmin());
            redirAttrs.addFlashAttribute("message", "The record has been deleted successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            redirAttrs.addFlashAttribute("message", "The ministry can not be deleted since it’s attached to an institute.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/ministryManagement/list";
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

        Page<MinistryEntity> minstrys = ministryService.findPaginated(evalPage, evalPageSize);
        Pager pager = new Pager(minstrys.getTotalPages(), minstrys.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("minstrys", minstrys);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pager", pager);
        model.addAttribute("isAdmin", isAdmin());
        return "MinistryManagementView";
    }
}
