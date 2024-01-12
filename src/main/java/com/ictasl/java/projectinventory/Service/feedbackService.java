package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.entity.FeedBack;
import com.ictasl.java.projectinventory.Persistence.entity.NewProject;
import com.ictasl.java.projectinventory.Web.dto.FeedbackDTO;
import com.ictasl.java.projectinventory.Web.dto.NewProjectDto;

import java.util.List;

public interface feedbackService {

    FeedbackDTO saveFeedback(FeedBack feedBack);

    List<FeedBack> findAllFeedbackByInstutionId(long id);

    void deleteFeedback(FeedBack feedBack);

}
