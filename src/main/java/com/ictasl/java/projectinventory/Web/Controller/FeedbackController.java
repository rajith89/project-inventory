package com.ictasl.java.projectinventory.Web.Controller;

import com.ictasl.java.projectinventory.Persistence.dao.FeedbackRepository;
import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.FeedBack;
import com.ictasl.java.projectinventory.Persistence.entity.ManageInstituteEntity;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Service.FeedbackServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/feedback")
public class FeedbackController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Autowired
    private FeedbackServiceImpl feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("feedbackDto", new FeedBack());
        List<FeedBack> feedback = feedbackService.findAllFeedbackByInstutionId(getInsId(session));
        model.addAttribute("feedbacks", feedback);
        return "feedback";
    }

    @PostMapping("/")
    public String saveFeedback(@Valid FeedBack feedBack, BindingResult bindingResult, RedirectAttributes redirAttrs, Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "The submitted data has errors.");
            redirAttrs.addFlashAttribute("error", "Error Saving...");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        } else {

            try {
                ManageInstituteEntity instituteEntity = new ManageInstituteEntity();
                instituteEntity.setId(getInsId(session));
                feedBack.setInstitution(instituteEntity);

                java.util.Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(dt);

                feedBack.setDateTime(currentTime);
                feedBack.setUserName(getUserName());
                model.addAttribute("feedback", feedbackService.saveFeedback(feedBack));
                model.addAttribute("successMessage", "The feedback has been added successfully.");
                redirAttrs.addFlashAttribute("message", "The feedback has been sent successfully.");
                redirAttrs.addFlashAttribute("alertClass", "alert-success");
                model.addAttribute("isAdmin", isAdmin());
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                redirAttrs.addFlashAttribute("error", "Error Saving Feedback...");
                redirAttrs.addFlashAttribute("alertClass", "alert-danger");
                model.addAttribute("isAdmin", isAdmin());
            }
        }
        return "redirect:/feedback/";
    }

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String listFeedback(ModelMap model, RedirectAttributes redirAttrs, HttpSession session) {
        try {
            List<FeedBack> feedback = feedbackService.findAllFeedbackByInstutionId(getInsId(session));
            model.addAttribute("feedbacks", feedback);
            model.addAttribute("metaTitle", "All New Feedbacks");
            model.addAttribute("isAdmin", isAdmin());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error occurred when fetching feedbacks");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            model.addAttribute("isAdmin", isAdmin());
        }

        return "feedbackview";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String removeFeedback(Model model, @PathVariable(required = true, name = "id") Long id, RedirectAttributes redirAttrs) {
        try {
            feedbackRepository.deleteById(id);
            redirAttrs.addFlashAttribute("message", "The feedback has been deleted successfully.");
            redirAttrs.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            redirAttrs.addFlashAttribute("message", "Error deleting feedback.");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }

        return "redirect:/feedback/list";
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

    private String getUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userRepository.findByUserName(username);

        }

        return user.getUserName();
    }

}
