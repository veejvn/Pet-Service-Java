package com.group.pet_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(basePackages = "com.group.pet_service.controller")
public class AdminExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ModelAndView handleAppException(AppException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        // Lấy tên trang hiện tại từ Request
        String currentPage = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (currentPage == null) {
            currentPage = "/admin/dashboard"; // Nếu không xác định được, quay về trang chủ hoặc trang mặc định
        }

        // Gắn thông báo lỗi vào Model
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.setViewName(currentPage); // Quay lại trang hiện tại
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        // Lấy tên trang hiện tại từ Request
        String currentPage = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (currentPage == null) {
            currentPage = "/admin/dashboard"; // Trang mặc định nếu không tìm thấy URI
        }

        // Gắn thông báo lỗi vào Model
        modelAndView.addObject("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        modelAndView.setViewName(currentPage); // Quay lại trang hiện tại
        return modelAndView;
    }
}

