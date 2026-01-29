package com.role.implementation.controller;

import com.role.implementation.devicemanagement.service.DeviceService;
import com.role.implementation.model.User;
import com.role.implementation.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String homePage(Model model, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        long totalDevices = deviceService.countTotalDevices();
        long activeDevices = deviceService.countActiveDevices();
        long inactiveDevices = totalDevices - activeDevices;

        model.addAttribute("userName", user.getName());
        model.addAttribute("totalDevices", totalDevices);
        model.addAttribute("activeDevices", activeDevices);
        model.addAttribute("inactiveDevices", inactiveDevices);

        return "home";
    }
}
