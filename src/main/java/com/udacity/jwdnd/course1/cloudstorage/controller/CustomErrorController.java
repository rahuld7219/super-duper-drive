package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    public String showErrorPage(HttpServletRequest httpServletRequest, Model model) {

        ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequest);
        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(servletWebRequest, true);

        model.addAttribute("error", "Something went wrong.");
        model.addAttribute("errorDetails", errorDetails);

        return "result";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
