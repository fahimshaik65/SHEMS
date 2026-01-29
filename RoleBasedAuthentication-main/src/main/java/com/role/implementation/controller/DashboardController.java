//package com.role.implementation.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.role.implementation.model.User;
//import com.role.implementation.repository.UserRepository;
//
//@Controller
//@RequestMapping("/dashboard")
//public class DashboardController {
//	
//	
//	@Autowired
//	private UserRepository userRepository;
//	
//	
//	@GetMapping
//    public String displayDashboard(Model model){
//		String user= returnUsername();
//        model.addAttribute("userDetails", user);
//        return "dashboard";
//    }
//	
//	private String returnUsername() {
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
//		User users = userRepository.findByEmail(user.getUsername());
//		return users.getName();
//	}
//		
//}


package com.role.implementation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.role.implementation.devicemanagement.service.DeviceService;
import com.role.implementation.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DeviceService deviceService;
    private final UserRepository userRepository;

    public DashboardController(DeviceService deviceService, UserRepository userRepository) {
        this.deviceService = deviceService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String displayDashboard(Model model) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = userRepository.findByEmail(email).getName();

        model.addAttribute("userDetails", username);

        // 🔹 Device info
        model.addAttribute("devices", deviceService.getDevicesForLoggedUser());
        model.addAttribute("totalDevices", deviceService.countTotalDevices());
        model.addAttribute("activeDevices", deviceService.countActiveDevices());

        return "dashboard";
    }
}
