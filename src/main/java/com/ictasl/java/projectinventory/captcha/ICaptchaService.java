package com.ictasl.java.projectinventory.captcha;


import com.ictasl.java.projectinventory.Web.error.ReCaptchaInvalidException;

public interface ICaptchaService {
    void processResponse(final String response) throws ReCaptchaInvalidException;

    String getReCaptchaSite();

    String getReCaptchaSecret();
}
