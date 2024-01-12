package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.FeedbackRepository;
import com.ictasl.java.projectinventory.Persistence.dao.InstituteManagementRepository;
import com.ictasl.java.projectinventory.Persistence.dao.NewProjectRepository;
import com.ictasl.java.projectinventory.Persistence.entity.*;
import com.ictasl.java.projectinventory.Security.MyUserDetails;
import com.ictasl.java.projectinventory.Web.dto.FeedbackDTO;
import com.ictasl.java.projectinventory.Web.dto.InstitutionDetailsDTO;
import com.ictasl.java.projectinventory.Web.dto.NewProjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("feedbackService")
public class FeedbackServiceImpl implements feedbackService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedbackRepository feedbackRepository;


    @Override
    public FeedbackDTO saveFeedback(FeedBack feedBackDto) {
        FeedBack feedBack = new FeedBack();
        FeedbackDTO feedbackDTOReturn = new FeedbackDTO();
        BeanUtils.copyProperties(feedBackDto,feedBack);
        feedBack = feedbackRepository.save(feedBack);
        BeanUtils.copyProperties(feedBack,feedbackDTOReturn);
        return feedbackDTOReturn;
    }

    @Override
    public List<FeedBack> findAllFeedbackByInstutionId(long id) {
        return feedbackRepository.findByInsId(id);
    }

    @Override
    public void deleteFeedback(FeedBack feedBack) {
        feedbackRepository.delete(feedBack);
    }
}
