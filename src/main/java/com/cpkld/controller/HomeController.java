package com.cpkld.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cpkld.model.CustomUserDetails;

@Controller
@RequestMapping("")
public class HomeController {

    @GetMapping("/")
    public String getHomePage(Model model) {
        String fullname = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullname();
        model.addAttribute("fullname", fullname.toUpperCase());
        return "homepage";
    }

    @GetMapping("/order")
    public String getOrderPage() {
        return "View/Order/index";
    }

    @GetMapping("/movie-list")
    public String getMovieListPage() {
        return "View/MovieList/index";
    }

    @GetMapping("/personal-profile")
    public String getPersonalProfilePage(Model model) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "View/Account/ThongTinCaNhan/index";
    }

    @GetMapping("/history")
    public String getHistoryPage() {
        return "View/Account/LichSuDatVe/index";
    }

    @GetMapping("/detail")
    public String getDetailpPage() {
        return "View/Detail/index";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "View/Contact/index";
    }
    
}
