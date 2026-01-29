package com.role.implementation.devicemanagement.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.role.implementation.devicemanagement.model.Device;
import com.role.implementation.devicemanagement.service.DeviceService;

@Controller
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // ================= USER DEVICES PAGE =================
    @GetMapping
    public String devicesPage(Model model) {
        model.addAttribute("devices", deviceService.getDevicesForLoggedUser());
        model.addAttribute("device", new Device());
        return "devices";
    }

    // ================= ADD NEW DEVICE =================
    @PostMapping("/add")
    public String addDevice(@Valid @ModelAttribute("device") Device device,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("devices", deviceService.getDevicesForLoggedUser());
            return "devices";
        }

        deviceService.addDevice(device);
        return "redirect:/devices?success";
    }

    // ================= USER TOGGLE =================
    @GetMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {
        deviceService.toggleDevice(id);
        return "redirect:/devices";
    }

    // ================= USER DELETE =================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return "redirect:/devices";
    }

    // =====================================================
    // 👑 ADMIN CONTROLS (redirect back to admin panel)
    // =====================================================

    @GetMapping("/admin/toggle/{id}")
    public String adminToggle(@PathVariable Long id) {
        deviceService.toggleDevice(id);
        return "redirect:/adminScreen/devices";
    }

    @GetMapping("/admin/delete/{id}")
    public String adminDelete(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return "redirect:/adminScreen/devices";
    }
}
