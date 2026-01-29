package com.role.implementation.devicemanagement.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.role.implementation.devicemanagement.model.Device;
import com.role.implementation.devicemanagement.repository.DeviceRepository;
import com.role.implementation.model.Role;
import com.role.implementation.model.User;
import com.role.implementation.repository.UserRepository;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepo;
    private final UserRepository userRepo;

    public DeviceServiceImpl(DeviceRepository deviceRepo, UserRepository userRepo) {
        this.deviceRepo = deviceRepo;
        this.userRepo = userRepo;
    }

    // 🔐 Get logged-in user
    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email);
    }

    // 👑 Check if ADMIN
    private boolean isAdmin(User user) {
        for (Role role : user.getRole()) {
            if (role.getRole().equals("ADMIN")) return true;
        }
        return false;
    }

    // ➕ Add new device (assigned to logged user)
    @Override
    public void addDevice(Device device) {
        device.setUser(getLoggedInUser());
        device.setStatus(false); // default OFF
        deviceRepo.save(device);
    }

    // 👤 USER → own devices | 👑 ADMIN → all devices
    @Override
    public List<Device> getDevicesForLoggedUser() {
        User user = getLoggedInUser();
        return isAdmin(user) ? deviceRepo.findAll() : deviceRepo.findByUser(user);
    }

    // 👑 ADMIN → get all devices (for admin panel)
    @Override
    public List<Device> getAllDevices() {
        return deviceRepo.findAll();
    }

    // 🔄 Toggle device ON/OFF
    @Override
    public void toggleDevice(Long id) {
        Device d = deviceRepo.findById(id).orElse(null);
        User user = getLoggedInUser();

        if (d != null && (isAdmin(user) || d.getUser().getId() == user.getId())) {
            d.setStatus(!d.isStatus());
            deviceRepo.save(d);
        }
    }

    // ❌ Delete device
    @Override
    public void deleteDevice(Long id) {
        Device d = deviceRepo.findById(id).orElse(null);
        User user = getLoggedInUser();

        if (d != null && (isAdmin(user) || d.getUser().getId() == user.getId())) {
            deviceRepo.delete(d);
        }
    }

    // 📊 DASHBOARD SUMMARY

    @Override
    public long countTotalDevices() {
        User user = getLoggedInUser();
        return isAdmin(user) ? deviceRepo.count() : deviceRepo.countByUser(user);
    }

    @Override
    public long countActiveDevices() {
        User user = getLoggedInUser();
        return isAdmin(user)
                ? deviceRepo.countByStatus(true)
                : deviceRepo.countByUserAndStatus(user, true);
    }
}
