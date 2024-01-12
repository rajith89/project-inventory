package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.ExistingSolutionService;
import com.ictasl.java.projectinventory.Web.dto.ExistingSolutionDto;
import com.ictasl.java.projectinventory.Web.dto.ReviewRequestDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
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
@RequestMapping("/existingsolution")
public class ExistingSolutionController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExistingSolutionService existingSolutionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ModelAndView existingSolutionForm() {
        ModelAndView modelAndView = new ModelAndView();
        ExistingSolutionDto existingSolutionDto = new ExistingSolutionDto();
        modelAndView.addObject("existingSolutionDto", existingSolutionDto);
        modelAndView.setViewName("existingSolutions");
        modelAndView.addObject("isAdmin", isAdmin());
        return modelAndView;
    }

    @GetMapping("/list")
    public String existingSolutionList(Model model,RedirectAttributes redirAttrs, HttpSession session) {
        try{
            model.addAttribute("existingSolutions", existingSolutionService.getAllSolutions(getInsId(session)));
        } catch (Exception ex){
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error occurred when fetching Exsiting Solutions.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        model.addAttribute("isAdmin", isAdmin());
        return "existingSolutionsList";
    }

    @PostMapping("/add")
    public String addExistingSolution(@Valid ExistingSolutionDto existing, BindingResult result, RedirectAttributes redirAttrs, Model model) {
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/existingSolution";
        }
        try{
            existingSolutionService.createExistingSolution(existing);
            redirAttrs.addFlashAttribute("message", "The record has been added successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        }catch (Exception ex){
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        model.addAttribute("isAdmin", isAdmin());
        return "redirect:/existingsolution/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model,RedirectAttributes redirAttrs){
        try {
            ExistingSolutionDto existingSolutionDto = existingSolutionService.getExistingSolution(id);
            model.addAttribute("existingSolutionDto", existingSolutionDto);
            model.addAttribute("isAdmin", isAdmin());
        }catch (ResourceNotFoundException ex){
            redirAttrs.addFlashAttribute("message", "Error occurred when fetching the Exisiting Solution");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            LOGGER.error(ex.getMessage());
        }
        model.addAttribute("isAdmin", isAdmin());
        return "existingSolutionsEdit";
    }

    @PostMapping("edit/{id}")
    public String updateExistingSolution(@PathVariable("id") long id, @Valid ExistingSolutionDto existing, RedirectAttributes redirAttrs, BindingResult result,
                                Model model, HttpSession session) {
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Error Updating...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            existing.setId(id);
            return "existingSolutionsEdit";
        }
        try {
            existingSolutionService.updateExistingSolution(id,existing);
            redirAttrs.addFlashAttribute("message", "The record has been updated successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        }catch (ResourceNotFoundException ex){
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error occurred when updating Existing Solutions.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }finally {
            model.addAttribute("existingSolutions", existingSolutionService.getAllSolutions(getInsId(session)));
        }
        model.addAttribute("isAdmin", isAdmin());
        return "existingSolutionsList";
    }

    @GetMapping("delete/{id}")
    public String deleteExistingSolution(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs, HttpSession session) {
        try{
            existingSolutionService.deleteExistingSolution(id);
            model.addAttribute("existingSolutions", existingSolutionService.getAllSolutions(getInsId(session)));
            redirAttrs.addFlashAttribute("message", "The record has been deleted successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        }catch (Exception ex){
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error Deleting Existing Solutions .");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        model.addAttribute("isAdmin", isAdmin());
        return "redirect:/existingsolution/list";
    }

    @PostMapping("/review")
    public ResponseEntity<Object> reviewExistingSolution(@Valid ReviewRequestDto reviewExisting, BindingResult result, RedirectAttributes redirAttrs, Model model) {
        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            //return "redirect:/existingSolution";
        }

        ExistingSolutionDto dto = new ExistingSolutionDto();
        try{
          dto =existingSolutionService.reviewExistingSolutoin(reviewExisting);

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
