package com.role.implementation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.role.implementation.model.User;
import com.role.implementation.repository.UserRepository;
import com.role.implementation.devicemanagement.service.DeviceService;

@Controller
@RequestMapping("/adminScreen")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceService deviceService;

    // 👑 ADMIN DASHBOARD
    @GetMapping
    public String displayDashboard(Model model) {

        String user = returnUsername();
        model.addAttribute("userDetails", user);

        long totalUsers = userRepository.count();
        long totalDevices = deviceService.countTotalDevices();
        long activeDevices = deviceService.countActiveDevices();
        long inactiveDevices = totalDevices - activeDevices;

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalDevices", totalDevices);
        model.addAttribute("activeDevices", activeDevices);
        model.addAttribute("inactiveDevices", inactiveDevices);

        model.addAttribute("users", userRepository.findAll());

        return "adminScreen";
    }

    // ❌ DELETE USER
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return "redirect:/adminScreen";
    }

    // 🏠 VIEW ALL DEVICES (ADMIN)
    @GetMapping("/devices")
    public String viewAllDevices(Model model) {

        // ✅ ADMIN should see ALL devices
        model.addAttribute("devices", deviceService.getAllDevices());

        return "adminDevices";
    }

    // 🔐 Get Logged-in Admin Name
    private String returnUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();

        User users = userRepository.findByEmail(user.getUsername());

        if (users == null) {
            return user.getUsername();
        }

        return users.getName();
    }
}
